package com.adupdate.sed_report_demo.entity.subscriber;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class SubscriberAbstract<T> implements Subscriber<T> {
    protected Subscription mSubscription;
    @Override
    public void onSubscribe(Subscription s) {
        mSubscription = s;
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
