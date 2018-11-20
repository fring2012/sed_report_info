package com.adupdate.sed_report_demo.entity.observer;



import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ObserverAbstract<T>  implements Observer<T> {
    protected Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T value) {

    }


    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
