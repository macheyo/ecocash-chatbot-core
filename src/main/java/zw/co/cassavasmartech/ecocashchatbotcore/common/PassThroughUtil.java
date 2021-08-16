package zw.co.cassavasmartech.ecocashchatbotcore.common;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

@Service
public class PassThroughUtil {

    public void forwardRequest(String uri, String method, HttpServletRequest req, HttpServletResponse resp) {

        final boolean hasoutbody = (method.equals("POST"));

        try {
            final URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            final Enumeration<String> headers = req.getHeaderNames();
            while (headers.hasMoreElements()) {
                final String header = headers.nextElement();
                final Enumeration<String> values = req.getHeaders(header);
                while (values.hasMoreElements()) {
                    final String value = values.nextElement();
                    conn.addRequestProperty(header, value);
                }
            }

            //conn.setFollowRedirects(false);  // throws AccessDenied exception
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(hasoutbody);
            conn.connect();

            final byte[] buffer = new byte[16384];
            while (hasoutbody) {
                final int read = req.getInputStream().read(buffer);
                if (read <= 0) break;
                conn.getOutputStream().write(buffer, 0, read);
            }

            resp.setStatus(conn.getResponseCode());
            for (int i = 0; ; ++i) {
                final String header = conn.getHeaderFieldKey(i);
                if (header == null) break;
                final String value = conn.getHeaderField(i);
                resp.setHeader(header, value);
            }

            while (true) {
                final int read = conn.getInputStream().read(buffer);
                if (read <= 0) break;
                resp.getOutputStream().write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // pass
        }
    }

}
