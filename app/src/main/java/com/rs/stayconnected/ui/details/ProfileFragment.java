package com.rs.stayconnected.ui.details;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.sumimakito.awesomeqr.AwesomeQRCode;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.mvc.imagepicker.ImagePicker;
import com.rs.stayconnected.BuildConfig;
import com.rs.stayconnected.R;
import com.rs.stayconnected.di.Injectable;
import com.rs.stayconnected.di.UserViewModelFactory;
import com.rs.stayconnected.entity.User;
import com.rs.stayconnected.ui.list.MainActivity;
import com.rs.stayconnected.utils.CommonUtils;
import com.rs.stayconnected.utils.ImageUtils;
import com.rs.stayconnected.viewmodel.UserDetailsViewModel;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import timber.log.Timber;

import static com.rs.stayconnected.utils.Constants.EMAIL_ID;
import static com.rs.stayconnected.utils.Constants.FACEBOOK_URL;
import static com.rs.stayconnected.utils.Constants.LINKEDIN_URL;
import static com.rs.stayconnected.utils.Constants.QRCODE;
import static com.rs.stayconnected.utils.Constants.TWITTER_URL;

/**
 * Created by ranjeetsinha on 11/01/18.
 */


public class ProfileFragment extends Fragment implements Injectable {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int FACEBOOK_DISCONNECT = 1;
    private static final int TWITTER_DISCONNECT = 2;
    private static final int LINKEDIN_DISCONNECT = 3;

    @Inject
    UserViewModelFactory userViewModelFactory;

    private ImageView profileImage;
    private Button cancelButton;
    private String name;
    private String emailId;
    private String phoneNo;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;

    private ImageView qrCode;

    private String facebookUrl;

    private String twitterUrl;

    private String linkedInProfileUrl;

    private TwitterAuthClient twitterClient;

    private ImageUtils imageUtils;

    private Bitmap profileIconBitmap;
    private User user;
    private UserDetailsViewModel userDetailsViewModel;


    private View.OnClickListener profileImageClickListener = view -> {
        if (!checkPermissions(Manifest.permission.CAMERA)) {
            requestPermissions(Manifest.permission.CAMERA);
        } else {
            ImagePicker.pickImage(ProfileFragment.this, getActivity().getString(R.string.profile_photo));
        }


    };

    private Button.OnClickListener saveButtonClickListener = (View view) -> {
        // Save data in DB;

        Editable nameText = nameEditText.getText();
        Editable emailText = emailEditText.getText();
        if (TextUtils.isEmpty(nameText)) {
            nameEditText.setError(getString(R.string.field_mandatory, getString(R.string.hint_name)));
            return;
        }
        if (TextUtils.isEmpty(emailText)) {
            emailEditText.setError(getString(R.string.field_mandatory, getString(R.string.hint_email)));
            return;
        }
        if (!CommonUtils.isValidEmail(emailText)) {
            emailEditText.setError(getString(R.string.invalid_email));
            return;
        }
        name = nameText.toString();
        emailId = emailText.toString();
        Timber.i("Email Id in Save Click %s", emailId);
        Editable phoneText = phoneEditText.getText();
        if (TextUtils.isEmpty(phoneText)) {
            phoneNo = null;
        } else {
            phoneNo = phoneText.toString();
        }
        if (profileIconBitmap != null && name != null) {
            imageUtils.saveBitmap(profileIconBitmap, emailId);

        }
        Timber.i("Inside save click %s", user);
        if (user != null) {
            userDetailsViewModel.updateUserProfileInfo(name, emailId, phoneNo, facebookUrl, linkedInProfileUrl, twitterUrl, null);
        } else {
            userDetailsViewModel.addUser(name, emailId, phoneNo, facebookUrl, linkedInProfileUrl, twitterUrl, false, null);
        }

        renderQRCode();


    };

    private void renderQRCode() {
        //TODO(RS): Run this on thread;
        //TODO(RS): Show data save successfully Toast;

        User user = new User(name, emailId, phoneNo, facebookUrl, linkedInProfileUrl, twitterUrl, false, null);

        new AwesomeQRCode.Renderer()
                .contents(user.toString())
                .size(800).margin(5)
                .renderAsync(new AwesomeQRCode.Callback() {
                    @Override
                    public void onRendered(AwesomeQRCode.Renderer renderer, final Bitmap bitmap) {

                        // Tip: here we use runOnUiThread(...) to avoid the problems caused by operating UI elements from a non-UI thread.

                        qrCode.setVisibility(View.VISIBLE);
                        qrCode.setImageBitmap(bitmap);
                        imageUtils.saveBitmap(bitmap, emailId + "_" + QRCODE);

                    }

                    @Override
                    public void onError(AwesomeQRCode.Renderer renderer, Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    private FacebookCallback<LoginResult> facebookCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            String userId = loginResult.getAccessToken().getUserId();
            facebookUrl = FACEBOOK_URL + userId;
            Timber.i("user id is %s", facebookUrl);

        }

        @Override
        public void onCancel() {
            Timber.i("Facebook Login Cancelled");

        }

        @Override
        public void onError(FacebookException error) {
            Timber.e(error.getMessage());

        }

    };
    private CallbackManager fbCallbackManager;


    private ImageButton.OnClickListener facebookProfileConnectListener = view -> {
        if (facebookUrl == null) {
            LoginManager.getInstance().logInWithReadPermissions(
                    this,
                    Arrays.asList("public_profile")
            );
            fbCallbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(fbCallbackManager, facebookCallBack);

        } else {
            // show Alert dialog to disconnect from facebook
            showDisconnectDialog(FACEBOOK_DISCONNECT);
        }

    };
    private Callback<TwitterSession> twitterCallback = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {

            twitterUrl = TWITTER_URL + result.data.getUserId();
            Timber.i("user id is %s", twitterUrl);

        }

        @Override
        public void failure(TwitterException exception) {
            Timber.e("exception %s", exception.getMessage());

        }
    };
    private ImageButton.OnClickListener twitterProfileConnectListener = view -> {
        twitterClient = new TwitterAuthClient();
        //TODO(RS): This should be handled in a better way; like if authorization is in Progress then
        // it should be cancelled and not everytime;
        if (twitterUrl == null) {
            twitterClient.cancelAuthorize();
            twitterClient.authorize(this.getActivity(), twitterCallback);
        } else {
            showDisconnectDialog(TWITTER_DISCONNECT);
        }

    };
    private ImageButton.OnClickListener linkedInProfileClickListener = view -> {
        //LISessionManager.getInstance(getActivity()).clearSession();


        AuthListener authListener = new AuthListener() {
            ApiListener linkedinProfileAPICallBack = new ApiListener() {
                @Override
                public void onApiSuccess(ApiResponse apiResponse) {
                    Timber.i("In Profile info success");
                    try {
                        JSONObject profileJSON = apiResponse.getResponseDataAsJson();
                        linkedInProfileUrl = profileJSON.get("publicProfileUrl").toString();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onApiError(LIApiError liApiError) {
                    Timber.e(liApiError.getMessage());

                }
            };

            @Override
            public void onAuthSuccess() {
                Timber.i("Authorisation successful");
                APIHelper apiHelper = APIHelper.getInstance(ProfileFragment.this.getActivity());
                apiHelper.getRequest(ProfileFragment.this.getActivity(), LINKEDIN_URL, linkedinProfileAPICallBack);
            }

            @Override
            public void onAuthError(LIAuthError liAuthError) {
                Timber.e("error %s", liAuthError.toString());

            }
        };
        if (linkedInProfileUrl == null && getActivity() != null) {
            LISessionManager.getInstance(this.getActivity()).init(this.getActivity(), Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE), authListener,
                    true);
        } else {
            showDisconnectDialog(LINKEDIN_DISCONNECT);
        }

    };


    private void showDisconnectDialog(int socialNetworkDisconnect) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this.getActivity());
        String title = null;
        int icon = 0;
        switch (socialNetworkDisconnect) {
            case FACEBOOK_DISCONNECT:
                title = getString(R.string.disconnect_string, "Facebook");
                icon = R.drawable.ic_fb;
                break;
            case TWITTER_DISCONNECT:
                title = getString(R.string.disconnect_string, "Twitter");
                icon = R.drawable.ic_twitter;
                break;
            case LINKEDIN_DISCONNECT:
                title = getString(R.string.disconnect_string, "Linkedin");
                icon = R.drawable.ic_linkedin;
                break;

            default:
                break;


        }

        int finalIcon = icon;
        builder.setTitle(title).setMessage(getString(R.string.confiramtion_message))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // continue with delete
                    switch (finalIcon) {
                        case R.drawable.ic_fb:
                            LoginManager.getInstance().logOut();
                            facebookUrl = null;
                            break;

                        case R.drawable.ic_twitter:
                            twitterClient.cancelAuthorize();
                            twitterUrl = null;
                            break;

                        case R.drawable.ic_linkedin:
                            LISessionManager.getInstance(getActivity()).clearSession();
                            linkedInProfileUrl = null;
                            break;

                    }
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(icon)
                .show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.i("Inside Profile Fragment");
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            emailId = args.getString(EMAIL_ID, null);
        }
        userDetailsViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserDetailsViewModel.class);

        initViews();
        if (emailId != null) {
            Timber.i("Email Id is %s", emailId);
            userDetailsViewModel.getUserDetails(emailId).observe(this, this::setupInitialData);


        }


    }

    private void initViews() {
        imageUtils = new ImageUtils(getContext());
        profileImage = this.getView().findViewById(R.id.profile_image);
        profileImage.setOnClickListener(profileImageClickListener);

        ImageButton facebookButton = this.getView().findViewById(R.id.facebook_button);
        facebookButton.setOnClickListener(facebookProfileConnectListener);

        ImageButton twitterButton = this.getView().findViewById(R.id.twitter_button);
        twitterButton.setOnClickListener(twitterProfileConnectListener);

        ImageButton linkedInButton = this.getView().findViewById(R.id.linkedin_button);
        linkedInButton.setOnClickListener(linkedInProfileClickListener);

        Button saveButton = this.getView().findViewById(R.id.save);
        saveButton.setOnClickListener(saveButtonClickListener);

        emailEditText = this.getView().findViewById(R.id.mail_edit_text);
        phoneEditText = this.getView().findViewById(R.id.phone_edit_text);
        nameEditText = this.getView().findViewById(R.id.profile_name_edit_text);

        qrCode = this.getView().findViewById(R.id.qr_code);


    }

    private void setupInitialData(User user) {
        Timber.i("User is %s", user);
        if (user != null) {
            this.user = user;
            emailEditText.setText(user.getEmailId());
            nameEditText.setText(user.getName());
            if (user.getPhoneNo() != null) {
                phoneEditText.setText(user.getPhoneNo());
            }
            facebookUrl = user.getFacebookUrl();
            linkedInProfileUrl = user.getLinkedinUrl();
            twitterUrl = user.getTwitterUrl();
            Bitmap profileImage = imageUtils.loadImageFile(user.getEmailId());
            if (profileImage != null) {
                this.profileImage.setImageBitmap(profileImage);

            }
            Bitmap qrImage = imageUtils.loadImageFile(user.getEmailId() + "_" + QRCODE);
            if (qrImage != null) {
                this.qrCode.setVisibility(View.VISIBLE);
                this.qrCode.setImageBitmap(qrImage);
            }

        }

    }

    private boolean checkPermissions(String permissionName) {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getActivity(),
                permissionName);
    }

    private void requestPermissions(String permissionName) {

        Timber.i("Requesting permission %s", permissionName);

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        permissionName);
        if (shouldProvideRationale) {
            // User clicked never ask Again;
            if (permissionName.equals(Manifest.permission.CAMERA)) {
                ImagePicker.pickImage(ProfileFragment.this, getActivity().getString(R.string.profile_photo));
            }

        } else {
            ActivityCompat.requestPermissions(ProfileFragment.this.getActivity(),
                    new String[]{permissionName},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this.getActivity(), requestCode, resultCode, data);
        Timber.i("Got bitmap . Save it to apps internal storage");
        if (bitmap != null) {

            //TODO(rs) : This needs to be done on Save button Click;
            profileIconBitmap = bitmap;
            profileImage.setImageBitmap(profileIconBitmap);
        }
        if (fbCallbackManager != null)
            fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (twitterClient != null) {
            twitterClient.onActivityResult(requestCode, resultCode, data);
        }
        LISessionManager.getInstance(this.getActivity()).onActivityResult(this.getActivity(),
                requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Timber.i("onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Timber.i("User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                // Add Camera Intent
                ImagePicker.pickImage(ProfileFragment.this, getActivity().getString(R.string.profile_photo));

            } else {
                // Permission denied.
                Snackbar.make(
                        getView().findViewById(R.id.profile_fragment),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        })
                        .show();
            }
        }


    }

}


