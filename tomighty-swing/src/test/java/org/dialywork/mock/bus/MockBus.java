package org.dialywork.mock.bus;

import java.util.ArrayList;
import java.util.List;

import org.dialywork.bus.Bus;
import org.dialywork.bus.Subscriber;
import org.dialywork.lang.NotImplementedYet;

public class MockBus implements Bus {

    private final List<Object> publishedMessages = new ArrayList<Object>();

    @Override
    public <T> void subscribe(Subscriber<T> subscriber, Class<T> messageType) {
        throw new NotImplementedYet();
    }

    @Override
    public <T> void unsubscribe(Subscriber<T> subscriber, Class<T> messageType) {
        throw new NotImplementedYet();
    }

    @Override
    public synchronized void publish(Object message) {
        publishedMessages.add(message);
        notifyAll();
    }

    public List<Object> getPublishedMessages() {
        return publishedMessages;
    }

    public synchronized List<Object> waitUntilNumberOfMessagesReach(int numberOfMessages) {
        while (publishedMessages.size() < numberOfMessages)
            waitQuietly();

        return publishedMessages;
    }

    private void waitQuietly() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
