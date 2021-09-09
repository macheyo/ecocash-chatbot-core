package zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore.data;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum ReversalStatus {

    PENDING, SUSPENSE, CANCELLED_BY_TIMEOUT, FAILED, APPROVED, PROCESSED, FAILED_REVERSAL_PENDING_MANUAL_REVERSAL;

    public static List<ReversalStatus> getPendingOrCompletedStatuses() {
        return new ArrayList<>(EnumSet.of(PENDING, SUSPENSE, APPROVED, PROCESSED));
    }

}
