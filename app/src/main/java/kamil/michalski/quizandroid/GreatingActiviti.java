package kamil.michalski.quizandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GreatingActiviti extends AppCompatActivity {
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_QUESTIONS = "questions";
    public static final String CURRENT_QUESTION = "currentQuestion";
    public static final String CHOICES = "choices";
    @BindView(R.id.question)
    TextView mQuestion;
    @BindView(R.id.answer_choice)
    RadioGroup mAnswers;
    //@BindView(R.id.answer_1)
    // RadioButton mAnswer1;
    //  @BindView(R.id.answer_2)
    // RadioButton mAnswer2;
    // @BindView(R.id.answer_3)
    // RadioButton mAnswer3;
    List<Question> mQuestions;
    @BindViews({R.id.answer_1, R.id.answer_2, R.id.answer_3})
    List<RadioButton> mRadioButtons;


    @BindView(R.id.back)
    Button mBackButton;
    @BindView(R.id.next)
    Button mNextButton;


    private int mCurrentQuestion;
    private int[] mChoices;
    private String mPlayerName;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greating_activiti);
        ButterKnife.bind(this);


        mPlayerName = getIntent().getStringExtra(EXTRA_NAME);
        mQuestions = (List<Question>) getIntent().getSerializableExtra(EXTRA_QUESTIONS);
        mChoices = new int[mQuestions.size()];


        refreshView();
        // mAnswer1.setText(question.getAnswer().get(0));
        // mAnswer2.setText(question.getAnswer().get(1));
        //mAnswer3.setText(question.getAnswer().get(2));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mChoices[mCurrentQuestion] = mAnswers.getCheckedRadioButtonId();
        outState.putInt(CURRENT_QUESTION, mCurrentQuestion);
        outState.putIntArray(CHOICES, mChoices);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentQuestion = savedInstanceState.getInt(CURRENT_QUESTION, 0);
        mChoices = savedInstanceState.getIntArray(CHOICES);

        refreshView();
    }

    @OnClick(R.id.next)
    void onNextClick() {
        mChoices[mCurrentQuestion] = mAnswers.getCheckedRadioButtonId();
        boolean isLastQuestion = mCurrentQuestion + 1 == mQuestions.size();
        if (isLastQuestion) {
            countResult();
            return;
        }
        mChoices[mCurrentQuestion] = mAnswers.getCheckedRadioButtonId();
        mCurrentQuestion++;
        refreshView();
    }

    @OnClick(R.id.back)
    void onBackClick() {
        // if (mCurrentQuestion - 1 < 0) {
        //   return;
        //   }
        mChoices[mCurrentQuestion] = mAnswers.getCheckedRadioButtonId();
        mCurrentQuestion--;
        refreshView();
    }


    private void refreshView() {
        Question question = mQuestions.get(mCurrentQuestion);

        mQuestion.setText(question.getQuestion());

        int index = 0;
        for (RadioButton rb : mRadioButtons) {
            rb.setText(question.getAnswer().get(index++));
        }


        mBackButton.setVisibility(mCurrentQuestion == 0 ? View.GONE : View.VISIBLE);
        mNextButton.setText(mCurrentQuestion == mQuestions.size() - 1 ? "zakoncz" : "dalej");


        mAnswers.clearCheck();
        if (mChoices[mCurrentQuestion] > 0) {
            mAnswers.check(mChoices[mCurrentQuestion]);
        }


    }

    private void countResult() {
        int correctAnswers = 0;
        int questionsCount = mQuestions.size();


        for (int i = 0; i < questionsCount; i++) {
            int correctAnswerIndex = mQuestions.get(i).getCorrectAnswer();
            int choiceRadoButtonId = mChoices[i];
            int choiceIndex = -1;
            for (int j = 0; j < mRadioButtons.size(); j++) {
                if (mRadioButtons.get(j).getId() == choiceRadoButtonId) {
                    choiceIndex = j;
                    break;
                }

            }
            if (correctAnswerIndex == choiceIndex) {
                correctAnswers++;
            }
        }

        //AlertDialog dialog=new AlertDialog.Builder(this)
        //  .setCancelable(false)
        // .setTitle("Wynik quizu")
        // .setMessage(String.format("Witaj %s ! Twój wynik to %d / %d !",
        //       mPlayerName,correctAnswers,questionsCount))
        //  .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        //     @Override
        //      public void onClick(DialogInterface dialog, int which) {
        //      finish();
        //    }
        //  })
        //         .create();
        //  dialog.show();


        QuizResultDialogFragment.createDialog(mPlayerName, correctAnswers, questionsCount)
                .show(getSupportFragmentManager(), "");


    }


}
