package vn.btec.campus_expense_management.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import vn.btec.campus_expense_management.R;
import vn.btec.campus_expense_management.entities.Transaction;
import vn.btec.campus_expense_management.utils.DataStatic;
import vn.btec.campus_expense_management.database.DatabaseHelper;

public class DashboardFragment extends Fragment {
    private RecyclerView rvRecentTransactions;
    private TransactionAdapter transactionAdapter;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        rvRecentTransactions = view.findViewById(R.id.rvRecentTransactions);
        rvRecentTransactions.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DatabaseHelper(getContext());
        User currentUser = DataStatic.getInstance().getCurrentUser();
        List<Transaction> transactionList = dbHelper.getTransactionList(currentUser.getUserId());

        transactionAdapter = new TransactionAdapter(transactionList);
        rvRecentTransactions.setAdapter(transactionAdapter);

        return view;
    }
}
