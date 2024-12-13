package vn.btec.campus_expense_management.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import vn.btec.campus_expense_management.R;
import vn.btec.campus_expense_management.utils.DataStatic;

public class LoginFragment extends Fragment {
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private CheckBox rememberMeCheckbox;
    private UserModel userModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailInput = view.findViewById(R.id.email);
        passwordInput = view.findViewById(R.id.password);
        rememberMeCheckbox = view.findViewById(R.id.cbRememberMe);
        userModel = new UserModel(requireContext());

        // Check saved credentials
        if (DataStatic.getInstance().checkSavedCredentials(requireContext())) {
            String[] credentials = DataStatic.getInstance().getSavedCredentials(requireContext());
            emailInput.setText(credentials[0]);
            passwordInput.setText(credentials[1]);
            rememberMeCheckbox.setChecked(true);
            performLogin(credentials[0], credentials[1], true);
        }

        view.findViewById(R.id.login_button).setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            boolean remember = rememberMeCheckbox.isChecked();
            performLogin(email, password, remember);
        });

        view.findViewById(R.id.goto_register).setOnClickListener(v -> {
            // Handle registration navigation
        });

        return view;
    }

    private void performLogin(String email, String password, boolean remember) {
        User user = userModel.getUserByEmailAndPassword(email, password);
        if (user != null) {
            DataStatic.getInstance().setCurrentUser(user);
            DataStatic.getInstance().setLogin(true);
            DataStatic.getInstance().saveLoginCredentials(requireContext(), email, password, remember);
            
            startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().finish();
        } else {
            Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
