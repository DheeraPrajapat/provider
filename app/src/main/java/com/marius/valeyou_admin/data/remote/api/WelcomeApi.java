package com.marius.valeyou_admin.data.remote.api;

import com.google.android.gms.common.api.Api;
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

import java.net.URI;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface WelcomeApi {

    @GET(Urls.GET_BANKS)
    Single<ApiResponse<List<Bank>>> getBanks(@Header("security_key") String security_key,
                                                  @Header("accept-language") String aaceptLanguage);

    @GET(Urls.GET_CATEGORIES)
    Single<ApiResponse<List<CategoryDataBean>>> getCategories(@Header("security_key") String security_key,
                                                              @Header("accept-language") String aaceptLanguage,
                                                              @Header("type") int type);

    @GET(Urls.GET_CATEGORIES)
    Single<ApiResponse<List<CategoryDataBean>>> getCategories(@Header("security_key") String security_key,
                                                              @Header("accept-language") String aaceptLanguage,
                                                              @Header("type") int type,

                                                              @Header("provider_id") String provider_id);


    @FormUrlEncoded
    @POST(Urls.CHECK_EMAIL)
    Single<SimpleApiResponse> checkEmail(@Header("security_key") String security_key,
                                         @Header("accept-language") String aaceptLanguage,
                                         @Field("email") String email);

    @FormUrlEncoded
    @POST(Urls.SEND_OTP)
    Single<SimpleApiResponse> sendOTP(@Header("security_key") String security_key,
                                      @Header("accept-language") String aaceptLanguage,
                                      @Field("email") String email);

    @FormUrlEncoded
    @POST(Urls.VERIFY_EMAIL)
    Single<SimpleApiResponse> verifyEmail(@Header("security_key") String security_key,
                                          @Header("accept-language") String aaceptLanguage,
                                          @Field("email") String email,
                                          @Field("otp") String otp);

    @FormUrlEncoded
    @POST(Urls.SIGNUP)
    Single<ApiResponse<SignInModel>> signup(@Header("security_key") String security_key,
                                            @Header("accept-language") String aaceptLanguage,
                                            @FieldMap Map<String, String> data);


    @FormUrlEncoded
    @POST(Urls.LOGIN)
    Single<ApiResponse<SignInModel>> singIn(@Header("security_key") String security_key,
                                            @Header("accept-language") String aaceptLanguage,
                                            @FieldMap Map<String, String> data);

    @PUT(Urls.LOGOUT)
    Single<ApiResponse<SignInModel>> logout(@Header("security_key") String security_key,
                                            @Header("accept-language") String aaceptLanguage,
                                            @Header("provider_id") String provider_id,
                                            @Header("auth_key") String auth_key);

    @FormUrlEncoded
    @POST(Urls.SOCIAL_LOGIN)
    Single<ApiResponse<SignInModel>> socialSignInApi(@Header("security_key") String security_key,
                                                     @Header("accept-language") String aaceptLanguage,
                                                     @FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST(Urls.FORGOT_PASSWORD)
    Single<ApiResponse<ForgotPasswordModel>> forgotPassword(@Header("security_key") String security_key,
                                                            @Header("accept-language") String aaceptLanguage,
                                                            @Field("email") String email);

    @FormUrlEncoded
    @PUT(Urls.CHANGE_PASSWORD)
    Single<ApiResponse<ChangePasswordModel>> ChangePassword(@Header("security_key") String security_key,
                                                            @Header("accept-language") String aaceptLanguage,
                                                            @Header("auth_key") String auth_key,
                                                            @Field("old_password") String old_password,
                                                            @Field("new_password") String new_password);

    @GET(Urls.PROFILE)
    Single<ApiResponse<ProfileModel>> getProfileData(@Header("security_key") String security_key,
                                                     @Header("accept-language") String aaceptLanguage,
                                                     @Header("auth_key") String auth_key,
                                                     @Header("provider_id") String provider_id);

    @GET(Urls.Aarin)
    Single<ApiResponse<Aarin>> getAarin(@Header("security_key") String security_key,
                                              @Header("accept-language") String aaceptLanguage,
                                              @Header("auth_key") String auth_key,
                                              @Header("AarinId") String AarinId);

    @GET(Urls.Adress)
    Single<ApiResponse<Adress>> getAdress(@Header("security_key") String security_key,
                                          @Header("accept-language") String aceptLanguage,
                                          @Header("cep") String cep);

    @GET(Urls.MAP_LIST)
    Single<ApiResponse<List<MapListModel>>> getMapList(@Header("security_key") String security_key,
                                                       @Header("accept-language") String aaceptLanguage,
                                                       @Header("auth_key") String auth_key,
                                                       @Query("search") String search);

    @POST(Urls.Add_PORTFOLIO)
    @Multipart
    Single<ApiResponse<JsonElement>> addPortfolio(@Header("security_key") String security_key,
                                                  @Header("accept-language") String aaceptLanguage,
                                                  @Header("auth_key") String auth_key,
                                                  @PartMap Map<String, RequestBody> data,
                                                  @Part MultipartBody.Part image);

    @POST(Urls.Add_Aarin)
    @Multipart
    Single<ApiResponse<JsonElement>> addAarin(@Header("security_key") String security_key,
                                                  @Header("accept-language") String aaceptLanguage,
                                                  @Header("auth_key") String auth_key,
                                                  @PartMap Map<String, RequestBody> data);

    @PUT(Urls.Add_Aarin)
    @Multipart
    Single<ApiResponse<JsonElement>> editAarin(@Header("security_key") String security_key,
                                              @Header("accept-language") String aaceptLanguage,
                                              @Header("auth_key") String auth_key,
                                              @PartMap Map<String, RequestBody> data);

    @GET(Urls.GET_LANGUAGES)
    Single<ApiResponse<List<LanguagesModel>>> getLanguagesApi(@Header("security_key") String security_key,
                                                              @Header("accept-language") String aaceptLanguage,
                                                              @Header("auth_key") String auth_key);

    @POST(Urls.ADD_LANGUAGE)
    @FormUrlEncoded
    Single<ApiResponse<JsonElement>> addLanguageApi(@Header("security_key") String security_key,
                                                    @Header("accept-language") String aaceptLanguage,
                                                    @Header("auth_key") String auth_key,
                                                    @Field("name") String name,
                                                    @Field("type") String type);

    @POST(Urls.ADD_CERTIFICATE)
    @Multipart
    Single<SimpleApiResponse> addCertificateApi(@Header("security_key") String security_key,
                                                @Header("accept-language") String aaceptLanguage,
                                                @Header("auth_key") String auth_key,
                                                @PartMap Map<String, RequestBody> data,
                                                @Part MultipartBody.Part image);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = Urls.DELETE_PORTFOLIO, hasBody = true)
    Single<SimpleApiResponse> deletePortfolio(@Header("security_key") String security_key,
                                              @Header("accept-language") String aaceptLanguage,
                                              @Header("auth_key") String auth_key,
                                              @Field("portfolio_id") int portfolio_id);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = Urls.DELETE_LANGUAGE, hasBody = true)
    Single<SimpleApiResponse> deleteLanguage(@Header("security_key") String security_key,
                                             @Header("accept-language") String aaceptLanguage,
                                             @Header("auth_key") String auth_key,
                                             @Field("lang_id") int lang_id);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = Urls.DELETE_CERTIFICATE, hasBody = true)
    Single<SimpleApiResponse> deleteCertificate(@Header("security_key") String security_key,
                                                @Header("accept-language") String aaceptLanguage,
                                                @Header("auth_key") String auth_key,
                                                @Field("certificate_id") int certificate);

    @PUT(Urls.EDIT_PROFILE)
    @FormUrlEncoded
    Single<SimpleApiResponse> editProfileServices(@Header("security_key") String security_key,
                                                  @Header("accept-language") String aaceptLanguage,
                                                  @Header("auth_key") String auth_key,
                                                  @Field("selected_data") String selected_data);

    @PUT(Urls.EDIT_PROFILE)
    @Multipart
    Single<ApiResponse<SignInModel>> editProfile(@Header("security_key") String security_key,
                                                 @Header("accept-language") String aaceptLanguage,
                                                 @Header("auth_key") String auth_key,
                                                 @PartMap Map<String, RequestBody> data,
                                                 @Part MultipartBody.Part image);

    @PUT(Urls.EDIT_PROFILE)
    @FormUrlEncoded
    Single<ApiResponse<SignInModel>> editProfileStr(@Header("security_key") String security_key,
                                                    @Header("accept-language") String aaceptLanguage,
                                                    @Header("auth_key") String auth_key,
                                                    @FieldMap Map<String, String> data);


    @FormUrlEncoded
    @PUT(Urls.EDIT_LANGUAGE)
    Single<SimpleApiResponse> editLanguage(@Header("security_key") String security_key,
                                           @Header("accept-language") String aaceptLanguage,
                                           @Header("auth_key") String auth_key,
                                           @Field("lang_id") int lang_id,
                                           @Field("name") String name,
                                           @Field("type") String type);

    @Multipart
    @PUT(Urls.EDIT_PORTFOLIO)
    Single<SimpleApiResponse> editPortfolio(@Header("security_key") String security_key,
                                            @Header("accept-language") String aaceptLanguage,
                                            @Header("auth_key") String auth_key,
                                            @PartMap Map<String, RequestBody> data,
                                            @Part MultipartBody.Part image
    );

    @Multipart
    @PUT(Urls.EDIT_CERTIFICATE)
    Single<SimpleApiResponse> editCertificate(@Header("security_key") String security_key,
                                              @Header("accept-language") String aaceptLanguage,
                                              @Header("auth_key") String auth_key,
                                              @PartMap Map<String, RequestBody> data,
                                              @Part MultipartBody.Part image
    );

    @GET(Urls.ALL_CONTENT)
    Single<ApiResponse<AboutModel>> aboutUsApi(@Header("security_key") String security_key,
                                               @Header("accept-language") String aaceptLanguage,
                                               @Header("type") int type);

    @GET(Urls.ALL_CONTENT)
    Single<ApiResponse<PrivacyModel>> privacyApi(@Header("security_key") String security_key,
                                                 @Header("accept-language") String aaceptLanguage,
                                                 @Header("type") int type);

    @GET(Urls.ALL_CONTENT)
    Single<ApiResponse<TermsModel>> termsApi(@Header("security_key") String security_key,
                                             @Header("accept-language") String aaceptLanguage,
                                             @Header("type") int type);


    @GET(Urls.MY_REVIEWS)
    Single<ApiResponse<List<MyReview>>> myReviewApi(@Header("security_key") String security_key,
                                                    @Header("accept-language") String aaceptLanguage,
                                                    @Header("auth_key") String auth_key);

    @GET(Urls.MY_FAVOURITES)
    Single<ApiResponse<List<MyFavouritesModel>>> myFavouritesApi(@Header("security_key") String security_key,
                                                                 @Header("accept-language") String aaceptLanguage,
                                                                 @Header("auth_key") String auth_key);

    @POST(Urls.Add_BUSSINESS_HOURS)
    @FormUrlEncoded
    Single<SimpleApiResponse> addBussinessHoursApi(@Header("security_key") String security_key,
                                                   @Header("accept-language") String aaceptLanguage,
                                                   @Header("auth_key") String auth_key, @Field("data") String data, @Field("type") String type);

    @GET(Urls.JOB_DETAIL)
    Single<ApiResponse<JobDetailModel>> getJobDetail(@Header("security_key") String security_key,
                                                     @Header("accept-language") String aaceptLanguage,
                                                     @Header("auth_key") String auth_key,
                                                     @Header("post_id") int post_id);

    @POST(Urls.PLACE_BID)
    @FormUrlEncoded
    Single<SimpleApiResponse> placeBid(@Header("security_key") String security_key,
                                       @Header("accept-language") String aaceptLanguage,
                                       @Header("auth_key") String auth_key,
                                       @Field("post_id") int post_id,
                                       @Field("price") int price,
                                       @Field("description") String description);

    @PUT(Urls.PROVIDER_UPDATE_BID)
    @FormUrlEncoded
    Single<SimpleApiResponse> updateBid(@Header("security_key") String security_key,
                                        @Header("accept-language") String aaceptLanguage,
                                        @Header("auth_key") String auth_key,
                                        @Field("post_id") int post_id,
                                        @Field("price") int price,
                                        @Field("description") String description);


    @GET(Urls.MY_JOBS)
    Single<ApiResponse<List<MyJobsModel>>> myJobsApi(@Header("security_key") String security_key,
                                                     @Header("accept-language") String aaceptLanguage,
                                                     @Header("auth_key") String auth_key,
                                                     @Query("status") String status);


    @GET(Urls.FILTER)
    Single<ApiResponse<List<MapListModel>>> filterApi(@Header("security_key") String security_key,
                                                      @Header("accept-language") String aaceptLanguage,
                                                      @Header("auth_key") String auth_key,
                                                      @Header("id") String id,
                                                      @Header("category_id") String category_id,
                                                      @Header("sub_cat") String sub_cat,
                                                      @Header("distance") String distance,
                                                      @Header("state_id") String state_id,
                                                      @Header("city_id") String city_id,
                                                      @Header("search") String search,
                                                      @Header("start_price") String start_price,
                                                      @Header("end_price") String end_price

    );


    @POST(Urls.ADD_IDENTITY)
    @Multipart
    Single<SimpleApiResponse> addIdentity(@Header("security_key") String security_key,
                                          @Header("accept-language") String aaceptLanguage,
                                          @Header("auth_key") String auth_key,
                                          @Part MultipartBody.Part image,
                                          @Part MultipartBody.Part backImage,
                                          @Part("vehicleType") RequestBody vehicleType,
                                          @Part  MultipartBody.Part selfie);

    @GET(Urls.GET_IDENTITY)
    Single<ApiResponse<List<IdentityModel>>> getIdentity(@Header("security_key") String security_key,
                                                         @Header("accept-language") String aaceptLanguage,
                                                         @Header("auth_key") String auth_key);


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = Urls.DELETE_IDENTITY, hasBody = true)
    Single<SimpleApiResponse> deleteIdentity(@Header("security_key") String security_key,
                                             @Header("accept-language") String aaceptLanguage,
                                             @Header("auth_key") String auth_key,
                                             @Field("identity_id") int certificate);

    @Multipart
    @PUT(Urls.EDIT_IDENTITY)
    Single<SimpleApiResponse> editIdentity(@Header("security_key") String security_key,
                                           @Header("accept-language") String aaceptLanguage,
                                           @Header("auth_key") String auth_key,
                                           @PartMap Map<String, RequestBody> data,
                                           @Part MultipartBody.Part image,
                                           @Part MultipartBody.Part backImage,
                                           @Part("vehicleType") RequestBody vehicleType);

    @GET(Urls.ALL_LANGUAGES)
    Single<ApiResponse<List<LanguagesBean>>> allLanguages(@Header("security_key") String security_key, @Header("accept-language") String aaceptLanguage);


    @POST(Urls.ACCOUNT_SETTING)
    Single<SimpleApiResponse> accountSetting(@Header("security_key") String security_key
            , @Header("accept-language") String aaceptLanguage,
                                             @Header("auth_key") String auth_key,
                                             @Header("type") String type);

    @POST(Urls.START_JOB)
    @FormUrlEncoded
    Single<SimpleApiResponse> startJob(@Header("security_key") String security_key,
                                       @Header("accept-language") String aaceptLanguage,
                                       @Header("auth_key") String auth_key,
                                       @Field("job_id") int job_id,
                                       @Field("status") int status,
                                       @Field("start_job_date") String start_job_date);


    @POST(Urls.START_JOB)
    @FormUrlEncoded
    Single<SimpleApiResponse> EndJob(@Header("security_key") String security_key,
                                     @Header("accept-language") String aaceptLanguage,
                                     @Header("auth_key") String auth_key,
                                     @Field("job_id") int job_id,
                                     @Field("status") int status,
                                     @Field("end_job_date") String end_job_date);


    @POST(Urls.ADD_TO_FAV)
    @FormUrlEncoded
    Single<SimpleApiResponse> addToFavApi(@Header("security_key") String security_key,
                                          @Header("accept-language") String aaceptLanguage,
                                          @Header("auth_key") String auth_key,
                                          @Field("job_id") int job_id,
                                          @Field("status") int status);


    @GET(Urls.GET_NOTIFICATION)
    Single<ApiResponse<List<NotificationModel>>> getNotifications(@Header("security_key") String security_key,
                                                                  @Header("accept-language") String aaceptLanguage,
                                                                  @Header("auth_key") String auth_key);


    @GET(Urls.FAQ)
    Single<ApiResponse<List<FaqModel>>> getFaq(@Header("security_key") String security_key,
                                               @Header("accept-language") String aaceptLanguage,
                                               @Header("auth_key") String auth_key);

    @POST(Urls.EDUCATION_ADD_EDIT)
    @FormUrlEncoded
    Single<SimpleApiResponse> addEditEducationApi(@Header("security_key") String security_key,
                                                  @Header("accept-language") String aaceptLanguage,
                                                  @Header("auth_key") String auth_key,
                                                  @FieldMap HashMap<String, String> map);

    @POST(Urls.EDUCATION_ADD_EDIT)
    @FormUrlEncoded
    Single<SimpleApiResponse> EditEducationApi(@Header("security_key") String security_key,
                                               @Header("accept-language") String aaceptLanguage,
                                               @Header("auth_key") String auth_key,
                                               @FieldMap HashMap<String, String> map,
                                               @Field("id") int id);


    @POST(Urls.EXPERIENCE_ADD_EDIT)
    @FormUrlEncoded
    Single<SimpleApiResponse> addEditExperienceApi(@Header("security_key") String security_key,
                                                   @Header("accept-language") String aaceptLanguage,
                                                   @Header("auth_key") String auth_key,
                                                   @FieldMap HashMap<String, String> map);


    @POST(Urls.EXPERIENCE_ADD_EDIT)
    @FormUrlEncoded
    Single<SimpleApiResponse> EditExperienceApi(@Header("security_key") String security_key,
                                                @Header("accept-language") String aaceptLanguage,
                                                @Header("auth_key") String auth_key,
                                                @FieldMap HashMap<String, String> map,
                                                @Field("id") int id);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = Urls.DELETE_EDUCATION, hasBody = true)
    Single<SimpleApiResponse> deleteEducation(@Header("security_key") String security_key,
                                              @Header("accept-language") String aaceptLanguage,
                                              @Header("auth_key") String auth_key,
                                              @Field("id") int id);


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = Urls.DELETE_EXPERIENCE, hasBody = true)
    Single<SimpleApiResponse> deleteExperience(@Header("security_key") String security_key,
                                               @Header("accept-language") String aaceptLanguage,
                                               @Header("auth_key") String auth_key,
                                               @Field("id") int id);


    @GET(Urls.INVOICE)
    Single<ApiResponse<InvoiceModel>> getInvoice(@Header("security_key") String security_key,
                                                 @Header("accept-language") String aaceptLanguage,
                                                 @Header("auth_key") String auth_key,
                                                 @Query("orderId") int orderId);

    @POST(Urls.PROVIDER_RATE_USER)
    @FormUrlEncoded
    Single<SimpleApiResponse> rateUser(@Header("security_key") String security_key,
                                       @Header("accept-language") String aaceptLanguage,
                                       @Header("auth_key") String auth_key,
                                       @Field("userTo") int userTo,
                                       @Field("orderId") int orderId,
                                       @Field("ratings") String ratings,
                                       @Field("description") String description);


    @GET(Urls.NOTIFICATION_BADGE_COUNT)
    Single<ApiResponse<NotificationBadgeModel>> getCount(@Header("security_key") String security_key,
                                                         @Header("accept-language") String aaceptLanguage,
                                                         @Header("auth_key") String auth_key);

    @GET(Urls.GET_STATES)
    Single<ApiResponse<List<StatesModel>>> getStates(@Header("security_key") String security_key,
                                                     @Header("accept-language") String aaceptLanguage,
                                                     @Header("auth_key") String auth_key,
                                                     @Query("search") String search);
    @GET(Urls.GET_STATES)
    Single<ApiResponse<List<StatesModel>>> getStates(@Header("security_key") String security_key,
                                                     @Header("accept-language") String aaceptLanguage);



    @GET(Urls.GET_CITIES)
    Single<ApiResponse<List<CitiesModel>>> getCities(@Header("security_key") String security_key,
                                                      @Header("accept-language") String acceptLanguage,
                                                      @Header("auth_key") String auth_key,
                                                      @Query("id") int id,
                                                      @Query("search") String search);
    @GET(Urls.GET_CITIES)
    Single<ApiResponse<List<CitiesModel>>> getCities(@Header("security_key") String security_key,
                                                      @Header("accept-language") String aaceptLanguage,
                                                      @Query("id") int id);



    @GET(Urls.PROVIDER_SUGGESTIONS)
    Single<ApiResponse<List<SuggesstionModel>>> getAllSuggestions(@Header("security_key") String security_key,
                                                                  @Header("accept-language") String aaceptLanguage,
                                                                  @Header("auth_key") String auth_key,
                                                                  @Header("search") String search);


    @GET(Urls.READ_NOTIFICATION)
    Single<SimpleApiResponse> readNotifications(@Header("security_key") String security_key,
                                                @Header("accept-language") String aaceptLanguage,
                                                @Header("auth_key") String auth_key,
                                                @Header("type") String type,
                                                @Header("notification_id") String notification_id);

    @FormUrlEncoded
    @PUT(Urls.UPDATE_CATEGORY_PRICE)
    Single<SimpleApiResponse> updatePrice(@Header("security_key") String security_key,
                                          @Header("accept-language") String aaceptLanguage,
                                          @Header("auth_key") String auth_key,
                                          @Field("category_id") String category_id,
                                          @Field("price") String price);

    @GET(Urls.PROVIDER_GET_USER_PROFILE)
    Single<ApiResponse<UserProfileModel>> getUserProfile(@Header("security_key") String security_key,
                                                         @Header("accept-language") String aaceptLanguage,
                                                         @Header("auth_key") String auth_key,
                                                         @Query("user_id") String user_id);


    @POST(Urls.BLOCK_USER)
    @FormUrlEncoded
    Single<SimpleApiResponse> blockUnblockUser(@Header("security_key") String security_key,
                                               @Header("accept-language") String aaceptLanguage,
                                               @Header("auth_key") String auth_key,
                                               @Field("user2Id") String user2Id,
                                               @Field("api_type") String api_type);


    @GET(Urls.BLOCK_LIST)
    Single<ApiResponse<List<BlockModel>>> getBlockList(@Header("security_key") String security_key,
                                                       @Header("accept-language") String aaceptLanguage,
                                                       @Header("auth_key") String auth_key);


    @GET(Urls.REJECTED_BIDS)
    Single<ApiResponse<List<RejectedModels>>> getRejectedBids(@Header("security_key") String security_key,
                                                              @Header("accept-language") String aaceptLanguage,
                                                              @Header("auth_key") String auth_key);


    @HTTP(method = "DELETE", path = Urls.DELETE_BID, hasBody = true)
    Single<SimpleApiResponse> deleteBid(@Header("security_key") String security_key,
                                        @Header("accept-language") String aaceptLanguage,
                                        @Header("auth_key") String auth_key,
                                        @Query("post_id") int post_id);


    @POST(Urls.CHANGE_STATUS)
    @FormUrlEncoded
    Single<SimpleApiResponse> changeStatus(@Header("security_key") String security_key,
                                           @Header("accept-language") String aaceptLanguage,
                                           @Header("auth_key") String auth_key,
                                           @Field("job_id") String order_id,
                                           @Field("type") String type,
                                           @Field("status") String status,
                                           @Field("reason") String reason);


    @POST(Urls.CHECK_CONFIRMATION_CODE)
    @FormUrlEncoded
    Single<SimpleApiResponse> checkConfirmationCode(@Header("security_key") String security_key,
                                                    @Header("accept-language") String aaceptLanguage,
                                                    @Header("auth_key") String auth_key,
                                                    @Field("order_id") String order_id,
                                                    @Field("confirmationCode") String confirmationCode);


    @POST(Urls.ADD_EXTRA_WORK)
    @FormUrlEncoded
    Single<SimpleApiResponse> addExtraWork(@Header("security_key") String security_key,
                                           @Header("accept-language") String aaceptLanguage,
                                           @Header("auth_key") String auth_key,
                                           @Field("order_id") String order_id,
                                           @Field("jsonData") String jsonData);


    @POST(Urls.NO_ACCESS_CHARGE)
    @FormUrlEncoded
    Single<SimpleApiResponse> addNoAccessCharge(@Header("security_key") String security_key,
                                                @Header("accept-language") String aaceptLanguage,
                                                @Header("auth_key") String auth_key,
                                                @Field("order_id") String order_id,
                                                @Field("description") String description,
                                                @Field("price") String price);

    @POST(Urls.RESCHEDULE_JOB)
    @FormUrlEncoded
    Single<SimpleApiResponse> rescheduleJob(@Header("security_key") String security_key,
                                            @Header("accept-language") String aaceptLanguage,
                                            @Header("auth_key") String auth_key,
                                            @Field("order_id") String order_id,
                                            @Field("type") String type,
                                            @Field("reScheduleDate") String reScheduleDate
    );

    @POST(Urls.CONFIRM_RESCHEDULE_JOB)
    @FormUrlEncoded
    Single<SimpleApiResponse> confirmReshedule(@Header("security_key") String security_key,
                                               @Header("accept-language") String acceptLanguage,
                                               @Header("auth_key") String auth_key,
                                               @Field("order_id") String order_id,
                                               @Field("type") String type);


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = Urls.DELETE_ADDITIONAL_COST, hasBody = true)
    Single<SimpleApiResponse> delete_additional_cost(@Header("security_key") String security_key,
                                                @Header("accept-language") String aaceptLanguage,
                                                @Header("auth_key") String auth_key,
                                                @Field("id") int id);




    @GET("get_comission")
    Single<ApiResponse<ComissionModel>> getComission(@Header("security_key") String security_key, @Header("accept-language") String acceptLanguage);



    @POST(Urls.UPLOAD_WORK_IMAGES)
    @Multipart
    Single<SimpleApiResponse> uploadWorkImages(@Header("security_key") String security_key,
                                               @Header("accept-language") String aaceptLanguage,
                                               @Header("auth_key") String auth_key,
                                               @Part("order_id") RequestBody order_id,
                                               @Part("type") RequestBody type,
                                               @Part("user_type") RequestBody user_type,
                                               @Part MultipartBody.Part image);


}
