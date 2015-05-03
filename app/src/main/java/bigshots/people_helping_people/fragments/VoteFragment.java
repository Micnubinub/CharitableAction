package bigshots.people_helping_people.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.CharityManager;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.utilities.Utility;
import bigshots.people_helping_people.utilities.VoteCharityAdapter;
import bigshots.people_helping_people.views.CharityListItemPedestal;

public class VoteFragment extends BaseFragment {
    public static CharityListItemPedestal pedestal;
    private static ListView listView;
    private static VoteCharityAdapter adapter;
    private static View message;

    public VoteFragment() {
    }

    public static void refreshList() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    message.setVisibility(View.GONE);
                    adapter = new VoteCharityAdapter(MainMenu.context, MainMenu.charities);
                    listView.setAdapter(adapter);
                    pedestal.setCharity(MainMenu.pedestal);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("vote", "failed to make adapter");
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
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Utility.hasActiveInternetConnection(view.getContext());
                    }
                });
            }
        });
        listView = (ListView) view.findViewById(R.id.list);
        pedestal = (CharityListItemPedestal) view.findViewById(R.id.pedestal);
        pedestal.setCharity(MainMenu.pedestal);
        return view;
    }


    private void showSuggestionDialog() {
        final Dialog dialog = new Dialog(MainMenu.context, R.style.CustomDialog);
        dialog.setContentView(R.layout.suggest_charity);

        final EditText charity_name = (EditText) dialog.findViewById(R.id.suggested_charity_name);
        final EditText charity_description = (EditText) dialog.findViewById(R.id.suggested_charity_description);
        final EditText charity_url = (EditText) dialog.findViewById(R.id.suggested_charity_url);
        final Button save = (Button) dialog.findViewById(R.id.submit_cancel).findViewById(R.id.submit);
        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String name = charity_name.getText().toString();
                final String desc = charity_description.getText().toString();

                save.setEnabled(!(name == null || name.length() < 2 || desc == null || desc.length() < 2));
                save.setTextColor(save.isEnabled() ? getResources().getColor(R.color.current_charity_color) : getResources().getColor(R.color.light_grey));
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
                        final String charityName = charity_name.getText().toString();
                        final String charityDescription = charity_description.getText().toString();
                        final String charityUrl = charity_url.getText().toString();
                        if (charityName != null && charityName.length() > 2) {
                            new CharityManager().suggestCharity(charityName, charityUrl, charityDescription);
                        }
                    case R.id.cancel:
                        dialog.dismiss();
                        break;
                }
            }
        };
        save.setOnClickListener(onClickListener);
        dialog.findViewById(R.id.submit_cancel).findViewById(R.id.cancel).setOnClickListener(onClickListener);
        save.setEnabled(false);
        save.setTextColor(getResources().getColor(R.color.light_grey));

        dialog.show();
    }

    @Override
    public void update() {
        if (adapter == null || MainMenu.charity == null) {
            MainMenu.downloadData();
            Log.e("returned", "adapter or charity is null");
            return;
        }
        refreshList();
        pedestal.setCharity(MainMenu.pedestal);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
