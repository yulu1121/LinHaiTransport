package com.anshi.linhaitransport.net;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface AppHttpService {
    @POST("user/login")
    Observable<ResponseBody> loginApp(@Query("username")String userName,@Query("password")String password);//登录
    @GET("case/caseType")
    Observable<ResponseBody> getCaseType();//获取案件类型
    @POST("upload/save")
    Observable<ResponseBody> uploadImage(@Body RequestBody requestBody);//保存图片
    @GET("user/getArea")
    Observable<ResponseBody> getArea();//获取区域
    @POST("case/add")
    Observable<ResponseBody> addCase(@Query("caseTitle")String caseTitle,@Query("caseType")String caseType,
                                     @Query("description")String desc,@Query("areaId")String areaId,
                                     @Query("createBy")String creatBy,@Query("longitude")String longitude,
                                     @Query("latitude")String latitude,@Query("address")String address,
                                     @Query("filepaths")String filepaths,
                                     @Query("patrolType")String patrolType);//新增案件
    @POST("case/findCase")
    Observable<ResponseBody> findCase(@Query("user_id")String userId);//查询列表（巡路员）
    @POST("case/findCaseById")
    Observable<ResponseBody> findCaseById(@Query("caseId")int caseId);//查询案件详情
    @POST("case/save")
    Observable<ResponseBody> saveCase(@Query("id")int caseId,@Query("userid")int userId);//上报
    @POST("case/del")
    Observable<ResponseBody> caseDel(@Query("caseid")int caseId);//删除
    @POST("case/findCLCase")
    Observable<ResponseBody> caseDealList(@Query("areaId")String areaId,@Query("deptId")String deptId,@Query("disposeType")String state);//不同上级部门查询列表
    @POST("case/reflect")
    Observable<ResponseBody> reflectCase(@Query("caseId")int caseId);//镇路长向上反映
    @POST("case/forward")
    Observable<ResponseBody> forwardCase(@Query("caseId")int caseId,@Query("deptId")int deptId);//转发
    @POST("case/dispose")
    Observable<ResponseBody> disPoseCase(@Query("caseId")int caseId,@Query("disposeOpinion")String opinion,@Query("deptId")int deptId,
                                        @Query("filePath")String filePath);//处理案件
    @GET("new/list")
    Observable<ResponseBody> newList();//获取新闻列表

    @GET("case/findForwardDept")
    Observable<ResponseBody> getDeptList();//获取部门列表
    @POST("case/closure")
    Observable<ResponseBody> endCase(@Query("caseId")int caseId,@Query("deptId")int deptId);//结案
    @POST("case/rehandling")
    Observable<ResponseBody> reHanling(@Query("caseId")int caseId,@Query("deptId")int deptId);//重新处理
    @GET("file/list")
    Observable<ResponseBody> getFileList();//获取文件列表
    @POST("map/location")
    Observable<ResponseBody> uploadLocation(@QueryMap Map<String,String> queryMap);//上传地点
    @POST("roadMarker/getAllRoadInfo")
    Observable<ResponseBody> getAllRoadInfo();//获取所有地点
    @POST("roadMarker/saveRoadApp")
    Observable<ResponseBody> saveRoadApp(@QueryMap Map<String,String> queryMap);



 }
