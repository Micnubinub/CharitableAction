package bigshots.people_helping_people.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.CharityManager;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.utilities.VoteCharityAdapter;

public class VoteFragment extends BaseFragment {
    private static ListView listView;
    private static VoteCharityAdapter adapter;
    private static View message;

    public VoteFragment() {
    }

    public static void refreshList() {
        //Todo test
        listView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    message.setVisibility(View.GONE);
                    adapter = new VoteCharityAdapter(MainMenu.context, MainMenu.charities);
                    listView.setAdapter(adapter);
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vote, container, false);
        message = view.findViewById(R.id.message);
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuggestionDialog();
            }
        });
        listView = (ListView) view.findViewById(R.id.list);
        return view;
    }


    private void showSuggestionDialog() {
        final Dialog dialog = new Dialog(MainMenu.context, R.style.CustomDialog);
        dialog.setContentView(R.layout.suggest_charity);
        final EditText charity_name = (EditText) dialog.findViewById(R.id.suggested_charity_name);
        final EditText charity_description = (EditText) dialog.findViewById(R.id.suggested_charity_description);
        final EditText charity_url = (EditText) dialog.findViewById(R.id.suggested_charity_url);
        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = charity_name.getText().toString();
                String desc = charity_description.getText().toString();
                dialog.findViewById(R.id.submit_cancel).findViewById(R.id.submit).setEnabled(!(name == null || name.length() < 2 || desc == null || desc.length() < 2));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        charity_description.addTextChangedListener(textWatcher);
        charity_name.addTextChangedListener(textWatcher);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.submit:
                        String charityName = charity_name.getText().toString();
                        String charityDescription = charity_description.getText().toString();
                        String charityUrl = charity_url.getText().toString();
                        //Todo
                        if (charityName != null && charityName.length() > 3) {
                            new CharityManager().suggestCharity(charityName, charityUrl, charityDescription);

                            Toast.makeText(MainMenu.context, "Thank you for your suggestion.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        break;
                    case R.id.cancel:
                        dialog.dismiss();
                        break;
                }
            }
        };
        dialog.findViewById(R.id.submit_cancel).findViewById(R.id.submit).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.submit_cancel).findViewById(R.id.cancel).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.submit_cancel).findViewById(R.id.submit).setEnabled(false);

        dialog.show();
    }

    @Override
    protected void update() {
        if (adapter == null || MainMenu.charity == null) {
            MainMenu.downloadData();
            return;
        }
        refreshList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
