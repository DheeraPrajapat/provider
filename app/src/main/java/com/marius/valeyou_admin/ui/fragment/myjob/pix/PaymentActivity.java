package com.marius.valeyou_admin.ui.fragment.myjob.pix;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
//import com.marius.valeyou_admin.data.beans.respbean.GetCardBean;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityPaymentBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
//import com.marius.valeyou_admin.ui.fragment.myjob.jobdetails.invoice.pyment.carddetails.paymentsuccess.PaymentSuccessActivity;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.databinding.ActivityPaymentBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
//import com.stripe_admin.android.Stripe;
//import com.stripe_admin.android.TokenCallback;
//import com.stripe_admin.android.model.Card;
//import com.stripe_admin.android.model.Token;

import java.util.List;


public class PaymentActivity extends AppActivity<ActivityPaymentBinding, PaymentActivityVM> {

    String amount;
    String cardNumber;
    int exp_month;
    int exp_year;
    String cvv="";
    int orderId;
    String servicefee;
    String cardId;
    String post_id;
    int jobType;
    String comeFrom;
    int cardPostion;

    public static Intent newIntnt(Activity activity, String comeFrom) {
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra("comeFrom",comeFrom);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

        @Override
        protected BindingActivity<PaymentActivityVM> getBindingActivity() {
            return new BindingActivity<>(R.layout.activity_payment, PaymentActivityVM.class);
        }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setToolbar() {
        binding.header.tvTitle.setText(getResources().getString(R.string.pagamento));
        binding.header.tvTitle.setTextColor(getResources().getColor(R.color.white));
        binding.header.setCheck(true);
    }
    @Override
        protected void subscribeToEvents(PaymentActivityVM vm) {

        setToolbar();
        Intent intent = getIntent();
        if (intent!=null){

            comeFrom = intent.getStringExtra("comeFrom");

            if (comeFrom.equalsIgnoreCase("home")){
//                binding.llButtons.setVisibility(View.GONE);
            }else{
//                binding.llButtons.setVisibility(View.VISIBLE);
            }

            amount = intent.getStringExtra("total");
            orderId = intent.getIntExtra("orderId",0);
            servicefee = intent.getStringExtra("fee");
            jobType = intent.getIntExtra("job_type",0);
            post_id = String.valueOf(orderId);
        }

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(this, permissions, 101);
            }

            binding.header.tvTitle.setText(getResources().getString(R.string.pagamento));
            binding.header.tvTitle.setTextColor(getResources().getColor(R.color.white));

            vm.base_back.observe(this, new Observer<Void>() {
                @Override
                public void onChanged(Void aVoid) {
                    finish(true);
                }
            });

            vm.base_click.observe(this, new Observer<View>() {
                @Override
                public void onChanged(View view) {
                    Intent intent;
                    switch (view != null ? view.getId() : 0) {
                        case R.id.btn_pay:
                            if (!cvv.isEmpty()&&cvv!=null) {
//                               onClickSomething(cardNumber, exp_month, exp_year, cvv,amount);

                            }else{

//                                vm.error.setValue(getResources().getString(R.string.click_on_saved_card_to_add_cvv));

                            }
                            break;
                        case R.id.btn_cancel:
                            finish(true);
                            break;
//                        case R.id.tv_add_newcard:
//                            intent = PaymentCardDetails.newIntent(PaymentActivity.this);
//                            startNewActivity(intent);
//                            break;
                    }
                }
            });



            vm.paymentEvent.observe(this, new Observer<Resource<SimpleApiResponse>>() {
                @Override
                public void onChanged(Resource<SimpleApiResponse> simpleApiResponseResource) {
                    switch (simpleApiResponseResource.status) {
                        case LOADING:
                            showProgressDialog(R.string.plz_wait);
                            break;
                        case SUCCESS:
                            dismissProgressDialog();
                            vm.success.setValue(simpleApiResponseResource.message);

                                Intent intent = MainActivity.newIntent(PaymentActivity.this);
                                startNewActivity(intent);
                                finishAffinity();

                            break;
                        case WARN:
                            dismissProgressDialog();
                            vm.warn.setValue(simpleApiResponseResource.message);
                            break;
                        case ERROR:
                            dismissProgressDialog();

                            vm.error.setValue(simpleApiResponseResource.message);
                            break;
                    }
                }
            });

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == 101 && grantResults.length > 0
                    && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                throw new RuntimeException("Location services are required in order to " +
                        "connect to a reader.");
            }
        }

//        private void setRecyclerView(List<GetCardBean> list) {
//        if (list.size()>0) {
//            cardsAdapter = new CardsAdaptere(PaymentActivity.this, list, this);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
////            binding.rvAddedCard.setLayoutManager(layoutManager);
////            binding.rvAddedCard.setAdapter(cardsAdapter);
//        }
//
//        }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
    }

//    public void onClickSomething(String cardNumber, int cardExpMonth, int cardExpYear, String cardCVC,String price) {
//        showProgressDialog(R.string.plz_wait);
//        Card card = new Card(cardNumber, cardExpMonth, cardExpYear, cardCVC);
//        card.validateNumber();
//        card.validateCVC();
//
//        if (!card.validateNumber()) {
//            viewModel.error.setValue(getResources().getString(R.string.car_number_not_valid));
//            dismissProgressDialog();
//            return;
//        } else if (!card.validateExpiryDate()) {
//            viewModel.error.setValue(getResources().getString(R.string.expire_date_not_valid));
//            dismissProgressDialog();
//            return;
//        } else if (!card.validateCVC()) {
//            viewModel.error.setValue(getResources().getString(R.string.cvv_not_valid));
//            dismissProgressDialog();
//            return;
//        } else if (card.validateCard()) {
//            //createToken(card);
//
//            viewModel.stripePaymentApi(orderId,cardId,price,servicefee,cvv);
//
//        } else {
//            dismissProgressDialog();
//            viewModel.error.setValue(getResources().getString(R.string.card_is_not_valid));
//        }
//
//    }
//
//    private void createToken(Card card) {
//        /*This is the TEST KEY for our stripe account - pk_test_UdHpc2MLTMd7FQzy1swFF5b3
//        SECRET KEY is sk_test_UgjtRADK4YlevK8iBwBYeJVJ*/
//        Stripe stripe = new Stripe(this, Constants.STRIPE_PUBLISHABLE_KEY);
//        stripe.createToken(
//                card,
//                new TokenCallback() {
//                    public void onSuccess(Token token) {
//                        dismissProgressDialog();
//                        String token_value = token.getId();
//                        viewModel.success.setValue(getResources().getString(R.string.payment_done));
//                        ///paymentMethod(orde_number, token.getId()); // hit backed api
//                        Intent intent = PaymentSuccessActivity.newIntent(PaymentActivity.this);
//                        startNewActivity(intent);
//                    }
//                    public void onError(Exception error) {
//                        viewModel.error.setValue(error.toString());
//                    }
//                }
//        );
//    }
//
//    @Override
//    public void onClickItemClickListner(int position, List<GetCardBean> morebeanList) {
//
//        cardId = String.valueOf(morebeanList.get(position).getId());
//       cardNumber =  morebeanList.get(position).getCardNumber();
//       exp_month = Integer.parseInt(morebeanList.get(position).getExpMonth());
//       exp_year =  Integer.parseInt(morebeanList.get(position).getExpYear());
//       cvv = morebeanList.get(position).getCvv();
//
//
//    }
//
//    @Override
//    public void onDeleteClickLisnter(int position, List<GetCardBean> morebeanList, int card_id) {
//            cardPostion = position;
//            viewModel.deleteCard(String.valueOf(card_id));
//
//    }


}
