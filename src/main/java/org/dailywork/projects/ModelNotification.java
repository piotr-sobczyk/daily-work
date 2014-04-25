package org.dailywork.projects;

public class ModelNotification<T> {

    private T model;
    private Object changeType;

    public ModelNotification(T model, Object changeType) {
        this.model = model;
        this.changeType = changeType;
    }

    public T getModel() {
        return model;
    }

    public Object getChangeType() {
        return changeType;
    }
}
