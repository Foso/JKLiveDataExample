package de.jensklingenberg.jklivedata;

import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subscribers.DisposableSubscriber;

public class MainActivity extends AppCompatActivity {

  JKViewModel mModel;
  @BindView(R.id.liveData1Headline) TextView liveData1Headline;
  @BindView(R.id.btnInc) Button btnInc;
  @BindView(R.id.btnDec) Button btnDec;
  @BindView(R.id.btnLayout) LinearLayout btnLayout;
  @BindView(R.id.count1IntegerTv) TextView count1IntegerTv;
  @BindView(R.id.countStringTv) TextView countStringTv;
  @BindView(R.id.valueLL) LinearLayout valueLL;
  @BindView(R.id.liveData2Headline) TextView liveData2Headline;
  @BindView(R.id.btnInc2) Button btnInc2;
  @BindView(R.id.btnDec2) Button btnDec2;
  @BindView(R.id.btnLayout2) LinearLayout btnLayout2;
  @BindView(R.id.secondTv) TextView secondTv;
  @BindView(R.id.liveData2Rl) RelativeLayout liveData2Rl;
  @BindView(R.id.liveDataMisc) TextView liveDataCombined;
  @BindView(R.id.mediatorLiveDataValueTv) TextView mediatorLiveDataValueTv;
  @BindView(R.id.fromFlowable) TextView fromFlowable;
  @BindView(R.id.rxJava) TextView rxJava;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    mModel = ViewModelProviders.of(this).get(JKViewModel.class);

    initObservers();
    btnInc.setOnClickListener(v -> mModel.incrementCount1());
    btnDec.setOnClickListener(v -> mModel.decrementCount1());
    btnInc2.setOnClickListener(view -> mModel.incrementCount2());
    btnDec2.setOnClickListener(view -> mModel.decrementCount2());
  }

  private void initObservers() {

    //Count1
    mModel.getCount()
        .observe(this, s -> count1IntegerTv.setText("count1 as Integer: " + String.valueOf(s)));
    mModel.getCount1AsString().observe(this, s -> countStringTv.setText("count1 as String: " + s));

    //Count2
    mModel.getCount2()
        .observe(this, integer -> secondTv.setText("count2: " + String.valueOf(integer)));

    //Misc
    mModel.getMediatorLiveData().observe(this, s -> {
      mediatorLiveDataValueTv.setText("MediatorLiveData:" + String.valueOf(s));
    });

    mModel.fromFlowable().observe(this, s -> {
      fromFlowable.setText("fromFlowable: " + s);
    });

    LiveDataReactiveStreams.toPublisher(this, mModel.getCount())
        .subscribe(new DisposableSubscriber<Integer>() {
          @Override public void onNext(Integer integer) {
            // Log.i("MainActivity", "onNext: " + String.valueOf(integer));
            rxJava.setText("LiveData to RxJava: " + String.valueOf(integer));
          }

          @Override public void onError(Throwable t) {
            Log.i("MainActivity", "onNext: ");
          }

          @Override public void onComplete() {
            Log.i("MainActivity", "onNext: ");
          }
        });
  }
}
