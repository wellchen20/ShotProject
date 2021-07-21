package com.jpkh.cnpc.activity.interfaces;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public interface RetrofitService {
   /* @GET("/bijia/car/info/searchCarByUserId")//get
    Call<CarEntity> getInfoData(@Query("userId") int userId);
    @POST("/asd/asd")//post
    Call<CarEntity> getExample(@Body CarEntity carEntity);

    @GET("/bijia/car/info/searchCarByUserId")//get

    Observable<CarEntity> getRxData(@Query("userId") int userId);
    @POST("doAllCardRecon")//身份证，驾驶证，行驶证，银行卡，车牌号上传
    Observable<ResponseBody> uploadjson(@Body RequestBody jsonBody);*/
   /*测试*/
   @GET("/toutiao/index")
   Observable<ResponseBody> getNews(@Query("type") String type, @Query("key") String key);

   /*上传工单和图片*/
   @Multipart
   @POST("/api/order")
   Observable<ResponseBody> uploadOrder(@Part(value = "order") RequestBody jsonBody, @Part List<MultipartBody.Part> file, @Part(value = "pic") RequestBody entity);

   /*上传图片*/
   @Multipart
   @POST("/api/upload")
   Observable<ResponseBody> uploadImages( @Part List<MultipartBody.Part> file);

   /*//   上传工单
   @POST("/api/uploadQcInfo")
   Observable<ResponseBody> upQcInfo(@Body RequestBody jsonBody);*/
   //   上传工单
   @Multipart
   @POST("/api/uploadQcInfo")
   Observable<ResponseBody> upQcInfo(@Part(value = "data") RequestBody jsonBody);

   /*问题上报*/
   @Multipart
   @POST("/api/question")
   Observable<ResponseBody> uploadQuestion(@Part(value = "question") RequestBody body, @Part List<MultipartBody.Part> file, @Part(value = "pic") RequestBody jsonBody);

  /* *//*工单列表*//*
   @GET("/api/orders/user/{user}")
   Observable<CheckListEntity> getCheckList(@Path("user") String user);

   *//*工单详情*//*
   @GET("/api/getorder/{workOrderId}")
   Observable<CheckContentEntity> getCheckContent(@Path("workOrderId") String workOrderId);

   *//*获取用户信息*//*
   @GET("/api/getaccessstationinfo/user/{user}")
   Observable<Userbean> getUserInfo(@Path("user") String user);*/

   /*进出站状态*/
   @Multipart
   @POST("/api/setaccessstationstatus")
   Observable<ResponseBody> Submitjc(@Part(value = "data") RequestBody body, @Part List<MultipartBody.Part> file, @Part(value = "pic") RequestBody jsonBody);

   //------------------------------------------------

   @POST("/user/login")
   Observable<ResponseBody> login(@Body RequestBody jsonBody);//登录

   @POST("/alarm/getAllStation")
   Observable<ResponseBody> getAllStation(@HeaderMap Map<String, String> headers);//获取桩号

   @POST("/alarm/firstGetAllImeis")
   Observable<ResponseBody> firstGetAllImeis(@HeaderMap Map<String, String> headers);//获取初始化告警信息

   @POST("/alarm/getBasicInfo")
   Observable<ResponseBody> getBasicInfo(@HeaderMap Map<String, String> headers);//获取设备数量信息

   @POST("/alarm/getAllImeiLocationLocal")
   Observable<ResponseBody> getAllImeiLocationLocal(@HeaderMap Map<String, String> headers);//获取所有在线设备

   @POST("/alarm/refreshAllDevice")
   Observable<ResponseBody> refreshAllDevice(@HeaderMap Map<String, String> headers);//刷新设备位置信息

   @POST("/alarm/getAll")
   Observable<ResponseBody> getAll(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//获取所有设备

   @POST("/alarm/getAllAlarmByImei")
   Observable<ResponseBody> getAllAlarmByImei(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//根据imei获取设备历史报警

   @POST("/alarm/getHistoryImeis")
   Observable<ResponseBody> getHistoryImeis(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//根据imei获取设备历史报警（新）

   @POST("/alarm/matchingStationAndImeiByHand")
   Observable<ResponseBody> matchingStationAndImeiByHand(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//绑定桩号与设备

   @POST("/alarm/delMatchByStation")
   Observable<ResponseBody> delMatchByStation(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//根据桩号解绑设备

   @POST("/alarm/delMatchByImei")
   Observable<ResponseBody> delMatchByImei(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//根据设备解绑桩号

   @POST("/alarm/toChangeHandleStatus")
   Observable<ResponseBody> toChangeHandleStatus(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//根据设备处理告警

   @POST("/alarm/toModifyRemark")
   Observable<ResponseBody> toModifyRemark(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//根据设备备注信息

   @POST("/alarm/saveAppLocation")
   Observable<ResponseBody> saveAppLocation(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//上传位置信息

   @POST("/alarm/getMessageHasNotRecieve")
   Observable<ResponseBody> getMessageHasNotRecieve(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//获取告警任务

   @POST("/alarm/listAppUser")
   Observable<ResponseBody> listAppUser(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//获取人员列表

   @POST("/alarm/addAppUser")
   Observable<ResponseBody> addAppUser(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//新增app子账户

   @POST("/alarm/delAppUser")
   Observable<ResponseBody> delAppUser(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//删除人员

   @POST("/alarm/toChangeAppUserStatus")
   Observable<ResponseBody> toChangeAppUserStatus(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//启用/禁用app用户

   @POST("/alarm/changeDeviceState")
   Observable<ResponseBody> changeDeviceState(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//撤防设防

    @POST("/alarm/getStationHasUpdated")
    Observable<ResponseBody> getStationHasUpdated(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//增量获取桩号信息

   @POST("/alarm/delStationCache")
   Observable<ResponseBody> delStationCache(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//清除桩号缓存

   @POST("/alarm/getStationById")
   Observable<ResponseBody> getStationById(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//根据station获取桩号信息

   @GET("/alarm/getDeviceInfoByImei")
   Observable<ResponseBody> getDeviceInfoByImei(@HeaderMap Map<String, String> headers,@Query("imei") String imei);//根据imei获取设备信息

   @POST("/alarm/getAllStationPage")
   Observable<ResponseBody> getAllStationPage(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//分页获取所有桩号信息

   @POST("/alarm/delStationCacheNew")
   Observable<ResponseBody> delStationCacheNew(@HeaderMap Map<String, String> headers,@Body RequestBody jsonBody);//清除桩号缓存(新增)
}
