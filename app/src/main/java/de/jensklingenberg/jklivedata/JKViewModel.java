package de.jensklingenberg.jklivedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class JKViewModel extends ViewModel {
  private static int count = 0;
  private static int secoundCount = 0;

  private static MutableLiveData<Integer> count1;
  private static MutableLiveData<Integer> count2;
  private MediatorLiveData<Integer> mediatorLiveData = new MediatorLiveData<>();

  public LiveData<Integer> getCount() {
    if (count1 == null) {
      count1 = new MutableLiveData<>();
    }
    return count1;
  }

  public LiveData<Integer> getCount2() {
    if (count2 == null) {
      count2 = new MutableLiveData<>();
    }
    return count2;
  }

  public void incrementCount1() {
    count++;
    count1.setValue(count);
  }

  public void incrementCount2() {
    secoundCount++;
    count2.setValue(secoundCount);
  }

  public void decrementCount2() {
    secoundCount--;
    count2.setValue(secoundCount);
  }

  public void decrementCount1() {
    count--;
    count1.setValue(count);
  }

  public LiveData<String> getCount1AsString() {
    return Transformations.map(getCount(), input -> String.valueOf(input));
  }

  public LiveData<String> fromFlowable() {
    return LiveDataReactiveStreams.fromPublisher(testFlowable());
  }

  public Flowable<String> testFlowable() {

    return Observable.just("Hello World")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .toFlowable(BackpressureStrategy.LATEST);
  }

  public MediatorLiveData<Integer> getMediatorLiveData() {
    mediatorLiveData.addSource(getCount(), integer -> mediatorLiveData.setValue(integer));
    mediatorLiveData.addSource(getCount2(), integer -> mediatorLiveData.setValue(integer));

    return mediatorLiveData;
  }
}
