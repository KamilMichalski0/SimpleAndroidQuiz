package kamil.michalski.quizandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartGameActivity extends AppCompatActivity {
    @BindView(R.id.name_field)
    EditText mEditText;
    @BindView(R.id.next_button)
    Button mNextButton;

    private IQuestionsDataBase mQuestionDataBase = new RandomQuestionsDataBase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.next_button)
    void openNextScreen() {

        String name = mEditText.getText().toString();
        Intent nameIntent = new Intent(this, GreatingActiviti.class);
        nameIntent.putExtra(GreatingActiviti.EXTRA_NAME, name);
        startActivity(nameIntent);

        List<Question> questions = mQuestionDataBase.getQuestions();
        Random random = new Random();
        while (questions.size() > 5) {
            questions.remove(random.nextInt(questions.size()));

        }
        Collections.shuffle(questions);

        nameIntent.putExtra(GreatingActiviti.EXTRA_QUESTIONS, new ArrayList<>(questions));
        startActivity(nameIntent);

    }

}
