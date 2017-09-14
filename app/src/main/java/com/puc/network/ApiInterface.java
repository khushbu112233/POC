package com.puc.network;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiInterface {


    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@Field("emailphone") String emailphone,
                             @Field("password") String password);

    @FormUrlEncoded
    @POST("registration")
    Call<ResponseBody> registration(@Field("email") String email,
                                    @Field("phone") String phone,
                                    @Field("password") String password);

    //for verify otp for security
    @FormUrlEncoded
    @POST("verifyotp")
    Call<ResponseBody> verifyotp(@Field("otp") String otp);


    //for bank list api
    @GET("banks")
    Call<ResponseBody> bankList(@Header("_token") String Authorization);

    //for add card api
    @FormUrlEncoded
    @POST("addcard")
    Call<ResponseBody> addCardDetail(@Header("_token") String _token,
                                     @Field("bank_id") String bank_id,
                                     @Field("card_number") String card_number,
                                     @Field("expiry_date") String expiry_date,
                                     @Field("card_name") String card_name,
                                     @Field("nick_name") String nick_name,
                                     @Field("street_address1") String street_address1,
                                     @Field("street_address2") String street_address2,
                                     @Field("city") String city,
                                     @Field("state") String state,
                                     @Field("zip_code") String zip_code,
                                     @Field("card_category") String card_category,
                                     @Field("card_type") String card_type,
                                     @Field("customer_service_number") String customer_service_number,
                                     @Field("type") String type,
                                     @Field("cvv_number") String cvv_number,
                                     @Field("email") String email,
                                     @Field("security_number") String security_number);


    //for list save card api..
    @GET("cards")
    Call<ResponseBody> saveCardList(@Header("_token") String Authorization);

    //for list card detail card api..
    @GET("lastcard")
    Call<ResponseBody> lastcardDetail(@Header("_token") String Authorization);


    //for edit card api
    @FormUrlEncoded
    @POST("editcard")
    Call<ResponseBody> editcardDetail(@Header("_token") String _token,
                                      @Field("id") String id,
                                      @Field("card_number") String card_number,
                                      @Field("expiry_date") String expiry_date,
                                      @Field("card_name") String card_name,
                                      @Field("nick_name") String nick_name,
                                      @Field("street_address1") String street_address1,
                                      @Field("street_address2") String street_address2,
                                      @Field("city") String city,
                                      @Field("state") String state,
                                      @Field("zip_code") String zip_code,
                                      @Field("card_category") String card_category,
                                      @Field("card_type") String card_type,
                                      @Field("customer_service_number") String customer_service_number,
                                      @Field("type") String type,
                                      @Field("cvv_number") String cvv_number,
                                      @Field("email") String email,
                                      @Field("security_number") String security_number);

    //for delete card
    @FormUrlEncoded
    @POST("deletecards")
    Call<ResponseBody> deleteCardList(@Header("_token") String _token,
                                      @Field("ids") String ids);


    //for add card api
    @FormUrlEncoded
    @POST("requestnewcard")
    Call<ResponseBody> requestForApplyNewCard(@Header("_token") String _token,
                                              @Field("bank_id") String bank_id,
                                              @Field("firstname") String firstname,
                                              @Field("middlename") String middlename,
                                              @Field("lastname") String lastname,
                                              @Field("birthdate") String birthdate,
                                              @Field("social_security_number") String social_security_number,
                                              @Field("address") String address,
                                              @Field("zipcode") String zipcode,
                                              @Field("email") String email,
                                              @Field("home_phone_number") String home_phone_number,
                                              @Field("state") String state);

    //for Cancel deactive card
    @FormUrlEncoded
    @POST("cancelcard")
    Call<ResponseBody> cancelcardDeactive(@Header("_token") String _token,
                                      @Field("ids") String id,
                                          @Field("lastfourdigits") String lastfourdigits);


    //for request new card if card lost or damamge.
    @FormUrlEncoded
    @POST("cardnewrequest")
    Call<ResponseBody> cardnewrequest(@Header("_token") String _token,
                                          @Field("ids") String id);


    //for cancel card verification password checking.
    @FormUrlEncoded
    @POST("checkpassword")
    Call<ResponseBody> checkpassword(@Header("_token") String _token,
                                      @Field("password") String password);


    //for request new card if card lost or damamge.
    @FormUrlEncoded
    @POST("changepassword")
    Call<ResponseBody> changepassword(@Header("_token") String _token,
                                      @Field("password") String password,
                                      @Field("newpassword") String newpassword);



    //for forgot password..
    @FormUrlEncoded
    @POST("forgetpassword")
    Call<ResponseBody> forgetpassword(@Field("emailphone") String emailphone);

    //for forgot password..
    @FormUrlEncoded
    @POST("resendotp")
    Call<ResponseBody> resendotp(@Field("userid") String userid);



    //for reset password api..
    @FormUrlEncoded
    @POST("usernewpassowrd")
    Call<ResponseBody> usernewpassowrd(@Field("userid") String userid,
                                      @Field("newpassword") String newpassword);


    //for logout api..
    @GET("logout")
    Call<ResponseBody> logout(@Header("_token") String Authorization);

    @FormUrlEncoded
    @POST("signal_provider/react_signal")
    Call<ResponseBody> react_signal(@Header("Authorization") String ssckey,
                                    @Field("spId") String spId,
                                    @Field("symbol") String symbol,
                                    @Field("stoploss") String stoploss,
                                    @Field("takeprofit") String takeprofit,
                                    @Field("order_type") String order_type,
                                    @Field("action") String action,
                                    @Field("valid_for") String valid_for,
                                    @Field("expiration") String expiration,
                                    @Field("price") String price);


    @GET("signal_provider/my_signal_provider_count")
    Call<ResponseBody> my_signal_provider_count(@Header("Authorization") String Authorization);


    @FormUrlEncoded
    @POST("signal_provider/update_signal_reacted")
    Call<ResponseBody> update_signal_reacted(@Header("Authorization") String ssckey,
                                             @Header("Content-Type") String Content_Type,
                                             @Field("signal_id") String signal_id,
                                             @Field("order_ticket") String order_ticket);

    @GET("signal_provider/my_signal_provider_status/{signal_id}")
    Call<ResponseBody> my_signal_provider_status(@Header("Authorization") String Authorization,
                                                 @Path("signal_id") String signal_id);


    @FormUrlEncoded
    @POST("signal_provider/get_signal_details")
    Call<ResponseBody> get_signal_details(@Header("Authorization") String ssckey,
                                          @Field("signal_id") String signal_id);

    @GET("signal_provider/react_signal_notification/{sdId}")
    Call<ResponseBody> react_signal_notification(@Header("Authorization") String Authorization,
                                                 @Path("sdId") String sdId);

    @FormUrlEncoded
    @POST("logout")
    Call<ResponseBody> logout(@Header("Authorization") String ssckey,
                              @Field("device_type") String device_type,
                              @Field("device_token") String device_token);

    @GET("country_list")
    Call<ResponseBody> country_list();


    //device mangager module api :
    //for pass qr code result send..
    @FormUrlEncoded
    @POST("tradeqrcode")
    Call<ResponseBody> tradeqrcode(@Header("Authorization") String ssckey,
                                   @Field("tradeAcc") String trade_acc,
                                   @Field("qrcode") String qr_code);


    //call every 7 second for checking qr code in db or not
    @GET("tradeqrcodecheck")
    Call<ResponseBody> check_tradeqrcodecheck(@Header("Authorization") String Authorization);


    //for get latest link trading account list
    @FormUrlEncoded
    @POST("canceltradeacclinked")
    Call<ResponseBody> canceltradeacclinkedapi(@Header("Authorization") String ssckey,
                                               @Field("qr_id") String qr_id);


    //for confirmation link trade account
    @FormUrlEncoded
    @POST("confirmtradeacclinked")
    Call<ResponseBody> confirmtradeacclinked(@Header("Authorization") String ssckey,
                                             @Field("authId") String authId,
                                             @Field("tradeAcc") String tradeAcc,
                                             @Field("tradePass") String tradePass, @Field("device_name") String device_name);


    //for confirmation link for smart watch  account
    @FormUrlEncoded
    @POST("smartwatch/confirmlinkedacc")
    Call<ResponseBody> confirmlinkedaccforsmartwatch(@Header("Authorization") String ssckey,
                                                     @Field("tradeAcc") String tradeAcc,
                                                     @Field("tradePass") String tradePass,
                                                     @Field("device_name") String device_name, @Field("device_id") String device_id, @Field("watch_device_type") String watch_device_type);


    //for get latest Unlink trading account list
    @FormUrlEncoded
    @POST("smartwatch/unlinkdevice")
    Call<ResponseBody> unlinkdeviceapi(@Header("Authorization") String ssckey,
                                       @Field("auth_link_id") String auth_link_id);


    //for get latest change device name of trading account list
    @FormUrlEncoded
    @POST("smartwatch/changelinkeddevicename")
    Call<ResponseBody> changelinkeddevicenameapi(@Header("Authorization") String ssckey,
                                                 @Field("auth_link_id") String auth_link_id, @Field("device_name") String device_name);


    //for get symbol list
    @FormUrlEncoded
    @POST("smartwatch/getsymbollist")
    Call<ResponseBody> getsymbollistapi(@Header("Authorization") String ssckey,
                                        @Field("tradeAcc") String tradeAcc);

    //for pass selected symbol list
    @FormUrlEncoded
    @POST("smartwatch/updatesubscribesym")
    Call<ResponseBody> updatesubscribesymapi(@Header("Authorization") String ssckey,
                                             @Field("tradeAcc") String tradeAcc, @Field("subscribe_sym") String subscribe_sym);


    //for update reset trade password
    @FormUrlEncoded
    @POST("smartwatch/update_trade_pass_linked_acc")
    Call<ResponseBody> updatetradepasslinkedaccApi(@Header("Authorization") String ssckey,
                                                   @Field("auth_link_id") String auth_link_id, @Field("tradeAcc") String tradeAcc, @Field("tradePass") String tradePass);

    /*BINALRY OPTION */

    @GET("bo/GetAllAccounts")
    Call<ResponseBody> GetAllBoAccount(@Header("Authorization") String Authorization);

    @FormUrlEncoded
    @POST("bo/AccountPasswordUpdate")
    Call<ResponseBody> BOAccountPasswordUpdate(@Header("Authorization") String Authorization,
                                               @Field("acc_no") String acc_no,
                                               @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST("bo/AddNewBOAcc")
    Call<ResponseBody> BoAccountAdd(@Header("Authorization") String Authorization,
                                    @Field("accType") String accType,
                                    @Field("accPass") String accPass);

    @FormUrlEncoded
    @POST("bo/GetTransferFundList")
    Call<ResponseBody> BOGetTrasferFundList(@Header("Authorization") String Authorization,
                                            @Field("keyword") String keyword,
                                            @Field("filter") String filter,
                                            @Field("limit") String limit,
                                            @Field("offset") String offset);


    @GET("bo/GetTransferAccountList")
    Call<ResponseBody> GetTransferAccountList(@Header("Authorization") String Authorization);

    @GET("bo/BOLogin")
    Call<ResponseBody> BOLogin(@Header("Authorization") String Authorization);

    @FormUrlEncoded
    @POST("bo/BORequestTransfer")
    Call<ResponseBody> BORequestTransfer(@Header("Authorization") String Authorization,
                                         @Field("transfer_from") String transfer_from,
                                         @Field("transfer_to") String transfer_to,
                                         @Field("transfer_amt") String transfer_amt,
                                         @Field("transfer_from_acc") String transfer_from_acc,
                                         @Field("transfer_to_acc") String transfer_to_acc);


}
