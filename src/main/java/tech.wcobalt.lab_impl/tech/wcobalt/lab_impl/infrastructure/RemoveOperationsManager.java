package tech.wcobalt.lab_impl.infrastructure;

import java.util.HashMap;
import java.util.Map;

public class RemoveOperationsManager {
    private Map<String, RemoveOperation> removeOperations;

    public RemoveOperationsManager() {
        removeOperations = new HashMap<>();
    }

    public void addRemoveOperation(String typeId, RemoveOperation removeOperation) {
        removeOperations.put(typeId, removeOperation);
    }

    public RemoveOperation getRemoveOperation(String typeId) {
        return removeOperations.get(typeId);
    }
}
