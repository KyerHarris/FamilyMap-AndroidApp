package kyer.harris.familymap.GUI;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Requests.*;
import Results.*;
import Model.*;
import kyer.harris.familymap.backend.*;
import kyer.harris.familymap.R;

public class LoginFragment extends Fragment {
    private Listener listener;
    private static final String IS_SUCCESS = "isSuccess";
    private static final String MESSAGE = "message";
    private static final String NAME = "name";
    private static EditText username;
    private static EditText password;
    private static EditText email;
    private static EditText firstName;
    private static EditText lastName;
    private static String gender = null;
    private RadioGroup genderGroup;
    private RadioGroup submitGroup;
    private boolean login;
    private Button submitButton;
    private Button registerButton;

    public interface Listener{
        void notifyDone();
    }
    public void registerListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Family Map");

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                checkWatchers();
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                checkWatchers();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                checkWatchers();
            }
        });
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                checkWatchers();
            }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                checkWatchers();
            }
        });

        genderGroup = view.findViewById(R.id.gender_group);
        genderGroup.clearCheck();
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                if(checkedId == R.id.radio_male){
                    gender = "m";
                    checkWatchers();
                }
                else{
                    gender = "f";
                    checkWatchers();
                }
            }
        });

        submitButton = view.findViewById(R.id.login_submit_button);
        submitButton.setEnabled(false);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    LoginTask task = new LoginTask(SubmitHandler);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);
                    PeopleTask peopleTask = new PeopleTask(peopleHandler, listener);
                    executor.execute(peopleTask);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        registerButton = view.findViewById(R.id.register_submit_button);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                try{
                    RegisterTask task = new RegisterTask(SubmitHandler);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);
                    PeopleTask peopleTask = new PeopleTask(peopleHandler, listener);
                    executor.execute(peopleTask);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
    private static class PeopleTask implements Runnable{
        private final Handler messageHandler;
        private Listener listener;

        public PeopleTask(Handler messageHandler, Listener listener){this.messageHandler = messageHandler; this.listener = listener;}
        @Override
        public void run() {
            if (DataCache.getInstance().getPersonID() != null) {
                PersonRequest personRequest = new PersonRequest();
                EventRequest eventRequest = new EventRequest();
                ServerProxy server = new ServerProxy();
                PersonResult personResult = server.getPersons(personRequest);
                EventResult eventResult = server.getEvents(eventRequest);
                Message message = Message.obtain();
                Bundle messageBundle = new Bundle();
                messageBundle.putBoolean(IS_SUCCESS, personResult.isSuccess());
                if (personResult.isSuccess() && eventResult.isSuccess()) {
                    DataCache.getInstance().setPeople(personResult.getData());
                    DataCache.getInstance().setEvents(eventResult.getData());
                    Person person = DataCache.getInstance().getPerson(DataCache.getInstance().getPersonID());
                    messageBundle.putString(NAME, person.getFirstName() + " " + person.getLastName());
                } else {
                    messageBundle.putString(MESSAGE, personResult.getMessage());
                }
                message.setData(messageBundle);
                messageHandler.sendMessage(message);
                listener.notifyDone();
            }
        }
    }

    private static class LoginTask implements Runnable{
        private final Handler messageHandler;

        public LoginTask(Handler messageHandler){
            this.messageHandler = messageHandler;
        }

        @Override
        public void run(){
            LoginRequest request = new LoginRequest(username.getText().toString(), password.getText().toString());
            ServerProxy server = new ServerProxy();
            LoginResult result = server.login(request);
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(IS_SUCCESS, result.isSuccess());
            messageBundle.putString(MESSAGE, result.getMessage());
            if(result.isSuccess()) {
                DataCache.getInstance().setAuthtoken(result.getAuthtoken());
                DataCache.getInstance().setPersonID(result.getPersonID());
            }
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }
    }

    private static class RegisterTask implements Runnable{
        private final Handler messageHandler;

        public RegisterTask(Handler messageHandler){
            this.messageHandler = messageHandler;
        }

        @Override
        public void run(){
            RegisterRequest request = new RegisterRequest(username.getText().toString(), password.getText().toString(), email.getText().toString(), firstName.getText().toString(), lastName.getText().toString(), gender);
            ServerProxy server = new ServerProxy();
            RegisterResult result = server.register(request);
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(IS_SUCCESS, result.isSuccess());
            messageBundle.putString(MESSAGE, result.getMessage());
            if(result.isSuccess()) {
                DataCache.getInstance().setAuthtoken(result.getAuthtoken());
                DataCache.getInstance().setPersonID(result.getPersonID());
            }
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }
    }
    Handler peopleHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            boolean success = bundle.getBoolean(IS_SUCCESS, false);
            String handlerMessage = bundle.getString(MESSAGE, null);
            String handlerUser = bundle.getString(NAME, null);
            if (success == false) {
                Context context = getView().getContext();
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(getActivity(), handlerMessage, duration).show();
            }
            else {
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(getActivity(), "name", duration).show();
            }
        }
    };

    Handler SubmitHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message){
            Bundle bundle = message.getData();
            boolean success = bundle.getBoolean(IS_SUCCESS, false);
            String handlerMessage = bundle.getString(MESSAGE, "");
            if(success == false){
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(getActivity(), handlerMessage, duration).show();
            }
        }
    };
    private void checkWatchers(){
        boolean login = true;
        boolean register = true;
        if(username.getText().toString().trim().length() == 0){
            login = false;
            register = false;
        }
        else if(password.getText().toString().trim().length() == 0){
            login = false;
            register = false;
        }
        else if(email.getText().toString().trim().length() == 0){
            register = false;
        }
        else if(firstName.getText().toString().trim().length() == 0){
            register = false;
        }
        else if(lastName.getText().toString().trim().length() == 0){
            register = false;
        }
        else if(gender == null){
            register = false;
        }

        if(login){
            submitButton.setEnabled(true);
        }
        if(register){
            registerButton.setEnabled(true);
        }
    }
}