package com.marius.valeyou_admin.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.airbnb.lottie.model.content.Mask;
import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.Aarin;
import com.marius.valeyou_admin.data.beans.Bank;
import com.marius.valeyou_admin.data.beans.CitiesModel;
import com.marius.valeyou_admin.data.beans.StatesModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.ApiUtils;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityStripeConnectBinding;
import com.marius.valeyou_admin.databinding.HolderCitiesBinding;
import com.marius.valeyou_admin.databinding.HolderStatesBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.LocationActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.editprofile.EditProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.editprofile.EditProfileActivityVM;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.AddPortfolioActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivityVM;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.permission.PermissionHandler;
import com.marius.valeyou_admin.util.permission.Permissions;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wdullaer.materialdatetimepicker.time.Timepoint;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class StripeConnectActivity extends AppActivity<ActivityStripeConnectBinding, StripeConnectActivityVM> {

    List<Bank> listaBancos;
    Bank bancoSelecionado;
    String comeFrom;
    String stripeId;
    Aarin aarin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.getBanks();
    }

    @Inject
    SharedPref sharedPref;
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, StripeConnectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<StripeConnectActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_stripe_connect, StripeConnectActivityVM.class);
    }

    private void configText(){
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mask = new MaskTextWatcher(binding.etCpf, smf);
        binding.etCpf.addTextChangedListener(mask);
        MaskTextWatcher mask2 = new MaskTextWatcher(binding.etPixCpf, smf);
        binding.etPixCpf.addTextChangedListener(mask2);

        SimpleMaskFormatter smf2 = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mask3 = new MaskTextWatcher(binding.etPixCel, smf2);
        binding.etPixCel.addTextChangedListener(mask3);
    }

    @Override
    protected void subscribeToEvents(StripeConnectActivityVM vm) {

        Intent intent = getIntent();
        if (intent!=null){
            comeFrom = intent.getStringExtra("comeFrom");
            if (comeFrom.equalsIgnoreCase("add")){
                binding.tvTitle.setText(getResources().getString(R.string.add_payment));
            }else{
                binding.tvTitle.setText(getResources().getString(R.string.edit_payment));
                stripeId = intent.getStringExtra("id");
                String authKey = vm.sharedPref.getUserData().getAuthKey();

                viewModel.profile(authKey, stripeId);
            }
        }

        binding.tvTitle.setTextColor(getResources().getColor(R.color.white));
//        binding.tvTitle.setText(getResources().getString(R.string.payment_information));
        binding.setCheck(true);
        configText();

//        String name = viewModel.sharedPref.getUserData().getName();
//        String cpf = viewModel.sharedPref.getUserData().getDriverLicence();
//        String banco = viewModel.sharedPref.getUserData().getStripeId();
//        String agencia = viewModel.sharedPref.getUserData().getHouseNumber();
//        String conta = viewModel.sharedPref.getUserData().getBusinessLicence();
//
//
//        binding.etName.setText(name);
//        binding.etCpf.setText(cpf);
//        //binding.etBank.setText(banco);
//        binding.etAgencia.setText(agencia);
//        binding.etConta.setText(conta);



        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                supportFinishAfterTransition();
                onBackPressed(true);
            }
        });

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view != null ? view.getId() : 0) {

                    case R.id.cv_save:

                        String authKey = vm.sharedPref.getUserData().getAuthKey();
                        String name = binding.etName.getText().toString();
                        String cpf = binding.etCpf.getText().toString();
                        //String banco = binding.etBank.getText().toString();
                        String agencia = binding.etAgencia.getText().toString();
                        String conta = binding.etConta.getText().toString();
                        int radioId = binding.rgPix.getCheckedRadioButtonId();
                        String pix = "";
                        String tipoChave = "";
                        switch (radioId){
                            case R.id.rbCpf:
                                pix = binding.etPixCpf.getText().toString().replace(".","").replace("-","");
                                tipoChave = "cpf";
                                break;

                            case R.id.rbCelular:
                                pix = "+55";
                                pix += binding.etPixCel.getText().toString().replace("(","").replace(")", "").replace("-","");
                                tipoChave = "phone";
                                break;

                            case R.id.rbEmail:
                                pix = binding.etPixEmail.getText().toString();
                                tipoChave = "cnpj";
                                break;

                            case R.id.rbAleatoria:
                                pix = binding.etPixAleatoria.getText().toString();
                                tipoChave = "evp";
                                break;
                        }

                        if (name.isEmpty() || agencia.isEmpty() || conta.isEmpty() || cpf.isEmpty() || pix.isEmpty() || bancoSelecionado == null){
                            vm.error.setValue(getResources().getString(R.string.preencha_todos_os_campos));
                            return;
                        }

                        Map<String, RequestBody> map =new HashMap<>();
                        map.put("nome",toRequestBody(name));
                        map.put("agencia",toRequestBody(agencia));
                        map.put("numeroConta",toRequestBody(conta));
                        map.put("ispb",toRequestBody(bancoSelecionado.getId()));
                        map.put("numeroDocumento",toRequestBody(cpf));
                        map.put("tipoDocumento",toRequestBody("cpf"));
                        map.put("tipoConta",toRequestBody("ContaCorrente"));
                        map.put("chave",toRequestBody(pix));
                        map.put("tipoChave",toRequestBody(tipoChave));


                        if (stripeId == null || stripeId.isEmpty()){
                            vm.cadastrar(authKey, map);
                        }
                        else{
                            vm.editar(authKey, map);
                        }



                        break;
                    case R.id.btnBanco:
                        setSpinner();
                        break;

                    case R.id.cv_cancel:
                        onBackPressed();
                        break;


                }
            }
        });

        vm.addAarin.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        if (resource.message != null && !resource.message.isEmpty()){
                            vm.error.setValue(resource.message);
                            return;
                        }
//                        Intent intent = ProfileActivity.newIntent(StripeConnectActivity.this);
//                        startNewActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(StripeConnectActivity.this);
                            startNewActivity(intent1, true, true);

                        }
                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });

        vm.profileBean.observe(this, new SingleRequestEvent.RequestObserver<Aarin>() {
            @Override
            public void onRequestReceived(@NonNull Resource<Aarin> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        aarin = resource.data;
                        fillFields();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(StripeConnectActivity.this);
                            startNewActivity(intent1, true, true);

                        }
                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });

        vm.bancos.observe(this, new SingleRequestEvent.RequestObserver<List<Bank>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<Bank>> resource) {
                switch (resource.status){
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        listaBancos = resource.data;
                        //setSpinner();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        break;
                    case WARN:
                        dismissProgressDialog();
                        binding.setCheck(false);
                        vm.warn.setValue(resource.message);
                        break;

                }
            }
        });

    }

    private void fillFields(){
        binding.etName.setText(aarin.getNome());
        binding.etAgencia.setText(aarin.getAgencia());
        binding.etConta.setText(aarin.getNumeroConta());
        binding.etCpf.setText(aarin.getNumeroDocumento());
        for (int i=0; i<listaBancos.size(); i++){
            if (aarin.getIspb().equals(listaBancos.get(i).getId())){
                binding.btnBanco.setText(listaBancos.get(i).getName());
            }
        }
        bancoSelecionado = new Bank(aarin.getIspb(), "banco");

        if (aarin.getTipoChave().toLowerCase().equals("phone")){
            binding.rbCelular.setChecked(true);
            binding.etPixCel.setText(aarin.getChave());
        }
        else if (aarin.getTipoChave().toLowerCase().equals("cpf")){
            binding.rbCpf.setChecked(true);
            binding.etPixCpf.setText(aarin.getChave());
        }
        else if (aarin.getTipoChave().toLowerCase().equals("cnpj")){
            binding.rbEmail.setChecked(true);
            binding.etPixEmail.setText(aarin.getChave());
        }
        else{
            binding.rbAleatoria.setChecked(true);
            binding.etPixAleatoria.setText(aarin.getChave());
        }
        dismissProgressDialog();
    }

    private void setSpinner(){
        List<String> listaItens = new ArrayList<>();
        for (Bank oItem: listaBancos)
        {
            listaItens.add(oItem.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaItens);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Qual seu banco?");

        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                bancoSelecionado = listaBancos.get(i);
                binding.btnBanco.setText(bancoSelecionado.getName());

                dialogInterface.dismiss();
            }
        });

        builder.create().show();
        //picker.show();
    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

}
