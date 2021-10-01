package com.marius.valeyou_admin.data.repo;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.marius.valeyou_admin.data.beans.Aarin;
import com.marius.valeyou_admin.data.beans.Adress;
import com.marius.valeyou_admin.data.beans.Bank;
import com.marius.valeyou_admin.data.beans.CitiesModel;
import com.marius.valeyou_admin.data.beans.ComissionModel;
import com.marius.valeyou_admin.data.beans.IdentityModel;
import com.marius.valeyou_admin.data.beans.LanguagesBean;
import com.marius.valeyou_admin.data.beans.NotificationBadgeModel;
import com.marius.valeyou_admin.data.beans.StatesModel;
import com.marius.valeyou_admin.data.beans.allcontent.AboutModel;
import com.marius.valeyou_admin.data.beans.allcontent.PrivacyModel;
import com.marius.valeyou_admin.data.beans.allcontent.TermsModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.block.BlockModel;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.change.ChangePasswordModel;
import com.marius.valeyou_admin.data.beans.faq.FaqModel;
import com.marius.valeyou_admin.data.beans.favourites.MyFavouritesModel;
import com.marius.valeyou_admin.data.beans.forgot.ForgotPasswordModel;
import com.marius.valeyou_admin.data.beans.invoice.InvoiceModel;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.beans.jobs.MyJobsModel;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.data.beans.notification.NotificationModel;
import com.marius.valeyou_admin.data.beans.profilebean.LanguagesModel;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.rejected_bids.RejectedModels;
import com.marius.valeyou_admin.data.beans.reviews.MyReview;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.beans.singninbean.SocialSignIn;
import com.marius.valeyou_admin.data.beans.suggesstions.SuggesstionModel;
import com.marius.valeyou_admin.data.beans.userprofile.UserProfileModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.api.WelcomeApi;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

public class WelcomeRepoImpl implements WelcomeRepo {

    private final SharedPref sharedPref;
    private final WelcomeApi welcomeApi;
    private final NetworkErrorHandler networkErrorHandler;

    public WelcomeRepoImpl(SharedPref sharedPref, WelcomeApi welcomeApi, NetworkErrorHandler networkErrorHandler) {
        this.sharedPref = sharedPref;
        this.welcomeApi = welcomeApi;
        this.networkErrorHandler = networkErrorHandler;
    }

    @Override
    public Single<ApiResponse<List<Bank>>> getBanks() {
        return welcomeApi.getBanks(Constants.SECURITY_KEY,sharedPref.get("language","en"));
    }

    @Override
    public Single<ApiResponse<List<CategoryDataBean>>> categoriesApi(int type) {
        return welcomeApi.getCategories(Constants.SECURITY_KEY,sharedPref.get("language","en"),type);
    }

    @Override
    public Single<ApiResponse<List<CategoryDataBean>>> categoriesApi(int type,String provider_id) {
        return welcomeApi.getCategories(Constants.SECURITY_KEY,sharedPref.get("language","en"),type,provider_id);
    }

    @Override
    public Single<SimpleApiResponse> checkEmail(String email) {
        return welcomeApi.checkEmail(Constants.SECURITY_KEY,sharedPref.get("language","en"),email);
    }

    @Override
    public Single<SimpleApiResponse> sendOTP(String email) {
        return welcomeApi.sendOTP(Constants.SECURITY_KEY,sharedPref.get("language","en"),email);
    }

    @Override
    public Single<SimpleApiResponse> verifyEmail(String email, String otp) {
        return welcomeApi.verifyEmail(Constants.SECURITY_KEY,sharedPref.get("language","en"),email,otp);
    }

    public Single<ApiResponse<SignInModel>> signup(Map<String, String> map) {
        return welcomeApi.signup(Constants.SECURITY_KEY,sharedPref.get("language","en"),map);
    }

    @Override
    public Single<ApiResponse<SignInModel>> signin(Map<String, String> map) {
        return welcomeApi.singIn(Constants.SECURITY_KEY,sharedPref.get("language","en"),map).doOnSuccess(userBeanApiResponse -> {
            if (userBeanApiResponse.getData() != null) {
                sharedPref.putUserData(userBeanApiResponse.getData());
            }
        });
    }

    @Override
    public Single<ApiResponse<SignInModel>> logout(String provider_id, String auth_key) {
        return welcomeApi.logout(Constants.SECURITY_KEY,sharedPref.get("language","en"),provider_id,auth_key);
    }

    @Override
    public Single<ApiResponse<SignInModel>> socialSignIn(Map<String, String> map) {
        return welcomeApi.socialSignInApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),map).doOnSuccess(userBeanApiResponse -> {
            if (userBeanApiResponse.getData() != null) {
                sharedPref.putUserData(userBeanApiResponse.getData());
            }
        });
    }

    @Override
    public Single<ApiResponse<ForgotPasswordModel>> forgotPassword(String email) {
        return welcomeApi.forgotPassword(Constants.SECURITY_KEY,sharedPref.get("language","en"),email);
    }

    @Override
    public Single<ApiResponse<ChangePasswordModel>> changePassword(String authKey, String oldPassword, String newPassord) {
        return welcomeApi.ChangePassword(Constants.SECURITY_KEY,sharedPref.get("language","en"),authKey,oldPassword,newPassord);
    }

    @Override
    public Single<ApiResponse<ProfileModel>> profile(String auth_key, String provider_id) {
        return welcomeApi.getProfileData(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,provider_id);
    }

    @Override
    public Single<ApiResponse<Aarin>> getAarin(String auth_key, String AarinId) {
        return welcomeApi.getAarin(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,AarinId);
    }

    @Override
    public Single<ApiResponse<Adress>> getAdress(String cep) {
        return welcomeApi.getAdress(Constants.SECURITY_KEY,sharedPref.get("language","en"),cep);
    }

    @Override
    public Single<ApiResponse<List<MapListModel>>> getMapList(String auth_key, String search) {
        return welcomeApi.getMapList(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,search);
    }

    @Override
    public Single<ApiResponse<JsonElement>> addPortfolio(String auth_key, Map<String, RequestBody> map, MultipartBody.Part imageFile) {
        return welcomeApi.addPortfolio(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map,imageFile);
    }

    @Override
    public Single<ApiResponse<JsonElement>> addAarin(String auth_key, Map<String, RequestBody> map) {
        return welcomeApi.addAarin(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map);
    }

    @Override
    public Single<ApiResponse<JsonElement>> editAarin(String auth_key, Map<String, RequestBody> map) {
        return welcomeApi.editAarin(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map);
    }

    @Override
    public Single<ApiResponse<List<LanguagesModel>>> getLanguagesApi(String auth_key) {
        return welcomeApi.getLanguagesApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key);
    }

    @Override
    public Single<ApiResponse<JsonElement>> addLanguageApi(String auth_key, String name, String type) {
        return welcomeApi.addLanguageApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,name,type);
    }

    @Override
    public Single<SimpleApiResponse> addCertificateApi(String auth_key, Map<String, RequestBody> map, MultipartBody.Part image) {
        return welcomeApi.addCertificateApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map,image);
    }

    @Override
    public Single<SimpleApiResponse> deletePortfolioApi(String auth_key, int portfolio_id) {
        return welcomeApi.deletePortfolio(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,portfolio_id);
    }

    @Override
    public Single<SimpleApiResponse> deleteLanguageApi(String auth_key, int lang_id) {
        return welcomeApi.deleteLanguage(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,lang_id);
    }

    @Override
    public Single<SimpleApiResponse> deleteCertificate(String auth_key, int certificate_id) {
        return welcomeApi.deleteCertificate(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,certificate_id);
    }

    @Override
    public Single<SimpleApiResponse> editProfileServicesApi(String auth_key, String selected_data) {
        return welcomeApi.editProfileServices(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,selected_data);

    }

    @Override
    public Single<SimpleApiResponse> editPortfolio(String auth_key, Map<String,RequestBody> map, MultipartBody.Part image) {
        return welcomeApi.editPortfolio(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map,image);

    }

    @Override
    public Single<SimpleApiResponse> editCertificate(String auth_key, Map<String, RequestBody> map, MultipartBody.Part image) {
        return welcomeApi.editCertificate(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map,image);
    }

    @Override
    public Single<ApiResponse<AboutModel>> aboutApi(int type) {
        return welcomeApi.aboutUsApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),type);
    }

    @Override
    public Single<ApiResponse<PrivacyModel>> privacyApi(int type) {
        return welcomeApi.privacyApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),type);
    }

    @Override
    public Single<ApiResponse<TermsModel>> termsApi(int type) {
        return welcomeApi.termsApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),type);
    }

    @Override
    public Single<ApiResponse<List<MyReview>>> myReviewApi(String auth_key) {
        return welcomeApi.myReviewApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key);
    }

    @Override
    public Single<ApiResponse<SignInModel>> editProfile(String auth_key, Map<String, RequestBody> map, MultipartBody.Part image) {
        return welcomeApi.editProfile(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map,image).doOnSuccess(userBeanApiResponse -> {
            if (userBeanApiResponse.getData() != null) {
                sharedPref.putUserData(userBeanApiResponse.getData());
            }
        });
    }

    @Override
    public Single<ApiResponse<SignInModel>> editProfileStr(String auth_key, Map<String, String> map) {
        return welcomeApi.editProfileStr(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map).doOnSuccess(userBeanApiResponse -> {
            if (userBeanApiResponse.getData() != null) {
                sharedPref.putUserData(userBeanApiResponse.getData());
            }
        });


    }

    @Override
    public Single<ApiResponse<List<MyFavouritesModel>>> myFavouritesApi(String auth_key) {
        return welcomeApi.myFavouritesApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key);
    }

    @Override
    public Single<SimpleApiResponse> addBussinessHoursApi(String auth_key, String data,String type) {
        return welcomeApi.addBussinessHoursApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,data, type);
    }

    @Override
    public Single<ApiResponse<JobDetailModel>> getJobDetailApi(String auth_key, int post_id) {
        return welcomeApi.getJobDetail(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,post_id);
    }

    @Override
    public Single<SimpleApiResponse> placeBid(String auth_key, int post_id, int price, String description) {
        return welcomeApi.placeBid(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,post_id,price,description);
    }

    @Override
    public Single<SimpleApiResponse> updateBid(String auth_key, int post_id, int price, String description) {
        return welcomeApi.updateBid(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,post_id,price,description);
    }

    @Override
    public Single<ApiResponse<List<MyJobsModel>>> myJobsApi(String auth_key, String status) {
        return welcomeApi.myJobsApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,status);
    }

    @Override
    public Single<ApiResponse<List<MapListModel>>> filterApi(String auth_key, String id, String category_id, String sub_cat, String distance, String state_id, String city_id, String search, String start_price, String end_price) {
        return welcomeApi.filterApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,id,category_id,sub_cat,distance,state_id,city_id,search,start_price,end_price);
    }



    @Override
    public Single<SimpleApiResponse> addIdentity(String auth_key, MultipartBody.Part image,MultipartBody.Part backImage,RequestBody vehicleType,MultipartBody.Part selfie) {
        return welcomeApi.addIdentity(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,image,backImage,vehicleType,selfie);
    }

    @Override
    public Single<ApiResponse<List<IdentityModel>>> getIdentity(String auth_key) {
        return welcomeApi.getIdentity(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key);
    }

    @Override
    public Single<SimpleApiResponse> deleteIdentity(String auth_key, int identity_id) {
        return welcomeApi.deleteIdentity(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,identity_id);
    }

    @Override
    public Single<SimpleApiResponse> editIdentity(String auth_key, Map<String, RequestBody> map, MultipartBody.Part image,MultipartBody.Part backImage,RequestBody vehicleType) {
        return welcomeApi.editIdentity(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map,image,backImage,vehicleType);
    }

    @Override
    public Single<ApiResponse<List<LanguagesBean>>> allLanguages() {
        return welcomeApi.allLanguages(Constants.SECURITY_KEY,sharedPref.get("language","en"));
    }

    @Override
    public Single<SimpleApiResponse> accountSetting(String auth_key, String type) {
        return welcomeApi.accountSetting(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,type);
    }

    @Override
    public Single<SimpleApiResponse> startJob(String auth_key, int job_id, int status,String start) {
        return welcomeApi.startJob(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,job_id,status,start);
    }

    @Override
    public Single<SimpleApiResponse> EndJob(String auth_key, int job_id, int status,String end) {
        return welcomeApi.EndJob(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,job_id,status,end);
    }

    @Override
    public Single<SimpleApiResponse> addToFavApi(String auth_key, int job_id, int status) {
        return welcomeApi.addToFavApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,job_id,status);
    }

    @Override
    public Single<ApiResponse<List<NotificationModel>>> getNotifications(String auth_key) {
        return welcomeApi.getNotifications(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key);
    }

    @Override
    public Single<SimpleApiResponse> readNotification(String auth_key, String type, String id) {
        return welcomeApi.readNotifications(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,type,id);
    }

    @Override
    public Single<ApiResponse<List<FaqModel>>> getFaq(String auth_key) {
        return welcomeApi.getFaq(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key);
    }

    @Override
    public Single<SimpleApiResponse> addEditEducationApi(String auth_key, HashMap<String, String> map) {
        return welcomeApi.addEditEducationApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map);
    }

    @Override
    public Single<SimpleApiResponse> EditEducationApi(String auth_key, HashMap<String, String> map, int id) {
        return welcomeApi.EditEducationApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map,id);
    }

    @Override
    public Single<SimpleApiResponse> addEditExperienceApi(String auth_key,HashMap<String, String> map) {
        return welcomeApi.addEditExperienceApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map);
    }

    @Override
    public Single<SimpleApiResponse> EditExperienceApi(String auth_key, HashMap<String, String> map, int id) {
        return welcomeApi.EditExperienceApi(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,map,id);
    }

    @Override
    public Single<SimpleApiResponse> deleteEducation(String auth_key, int id) {
        return welcomeApi.deleteEducation(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,id);
    }

    @Override
    public Single<SimpleApiResponse> deleteExperience(String auth_key, int id) {
        return welcomeApi.deleteExperience(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,id);
    }

    @Override
    public Single<ApiResponse<InvoiceModel>> getInvoice(String auth_key, int orderId) {
        return welcomeApi.getInvoice(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,orderId);
    }

    @Override
    public Single<SimpleApiResponse> rateUser(String auth_key, int userTo,int orderId, String rating, String description) {
        return welcomeApi.rateUser(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,userTo,orderId,rating,description);
    }

    @Override
    public Single<ApiResponse<NotificationBadgeModel>> getCount(String auth_key) {
        return welcomeApi.getCount(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key);
    }

    @Override
    public Single<ApiResponse<List<StatesModel>>> getStates() {
        return welcomeApi.getStates(Constants.SECURITY_KEY,sharedPref.get("language","en"));
    }
    @Override
    public Single<ApiResponse<List<StatesModel>>> getStates(String auth_key,String search) {
        return welcomeApi.getStates(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,search);
    }

    @Override
    public Single<ApiResponse<List<CitiesModel>>> getCities(int id) {
        return welcomeApi.getCities(Constants.SECURITY_KEY,sharedPref.get("language","en"),id);
    }
    @Override
    public Single<ApiResponse<List<CitiesModel>>> getCities(String auth_key ,int id,String search) {
        return welcomeApi.getCities(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,id,search);
}


    @Override
    public Single<ApiResponse<List<SuggesstionModel>>> getAllSuggestions(String auth_key, String search) {
        return welcomeApi.getAllSuggestions(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,search);
    }

    @Override
    public Single<SimpleApiResponse> updatePrice(String auth_key, String category_id, String price) {
        return welcomeApi.updatePrice(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,category_id,price);
    }

    @Override
    public Single<ApiResponse<UserProfileModel>> getUserProfile(String auth_key, String user_id) {
        return welcomeApi.getUserProfile(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,user_id);
    }

    @Override
    public Single<SimpleApiResponse> blockUnblockUser(String auth_key, String user2Id, String api_type) {
        return welcomeApi.blockUnblockUser(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,user2Id,api_type);
    }

    @Override
    public Single<SimpleApiResponse> editLanguage(String auth_key, int lang_id, String name, String type) {
        return welcomeApi.editLanguage(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,lang_id,name,type);
    }

    @Override
    public Single<ApiResponse<List<BlockModel>>> blockList(String auth_key) {
        return welcomeApi.getBlockList(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key);
    }

    @Override
    public Single<ApiResponse<List<RejectedModels>>> getRejectedBids(String auth_key) {
        return welcomeApi.getRejectedBids(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key);
    }

    @Override
    public Single<SimpleApiResponse> deleteBid(String auth_key, int post_id) {
        return welcomeApi.deleteBid(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,post_id);
    }

    @Override
    public Single<SimpleApiResponse> changeStatus(String auth_key, String order_id, String type, String status,String reason) {
        return welcomeApi.changeStatus(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,order_id,type,status,reason);
    }

    @Override
    public Single<SimpleApiResponse> checkConfirmationCode(String auth_key, String order_id, String confirmationCode) {
        return welcomeApi.checkConfirmationCode(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,order_id,confirmationCode);
    }

    @Override
    public Single<SimpleApiResponse> addExtraWork(String auth_key, String order_id, String jsonData) {
        return welcomeApi.addExtraWork(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,order_id,jsonData);
    }

    @Override
    public Single<SimpleApiResponse> addNoAccessCharge(String auth_key, String order_id, String description, String price) {
        return welcomeApi.addNoAccessCharge(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,order_id,description,price);
    }

    @Override
    public Single<SimpleApiResponse> rescheduleJob(String auth_key, String order_id,String type ,String reScheduleDate) {
        return welcomeApi.rescheduleJob(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,order_id,type,reScheduleDate);
    }

    @Override
    public Single<SimpleApiResponse> confirmRescheduleJob(String auth_key, String order_id, String type) {
        return welcomeApi.confirmReshedule(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,order_id,type);
    }

    @Override
    public Single<SimpleApiResponse> delete_additional_cost(String auth_key, int id) {
        return welcomeApi.delete_additional_cost(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,id);
    }

    @Override
    public Single<ApiResponse<ComissionModel>> getComission() {
        return welcomeApi.getComission(Constants.SECURITY_KEY,sharedPref.get("language","en"));
    }

    @Override
    public Single<SimpleApiResponse> uploadWorkImages(String auth_key, RequestBody order_id, RequestBody type, RequestBody user_type, MultipartBody.Part image) {
        return welcomeApi.uploadWorkImages(Constants.SECURITY_KEY,sharedPref.get("language","en"),auth_key,order_id,type,user_type,image);
    }


}
