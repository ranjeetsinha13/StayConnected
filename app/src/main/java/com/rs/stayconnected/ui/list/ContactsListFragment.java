package com.rs.stayconnected.ui.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rs.stayconnected.R;
import com.rs.stayconnected.di.Injectable;
import com.rs.stayconnected.di.UserViewModelFactory;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by ranjeetsinha on 11/01/18.
 */

public class ContactsListFragment extends Fragment implements Injectable {

    @Inject
    UserViewModelFactory userViewModelFactory;
    private View.OnClickListener addContactButtonClickListener = v -> {
        // Open Barcode scanner/ Camera

    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.i("Inside Contacts list  Fragment");
        return inflater.inflate(R.layout.fragment_contacts_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        Button addContactButton = this.getView().findViewById(R.id.addContact);
        addContactButton.setOnClickListener(addContactButtonClickListener);
    }
}
