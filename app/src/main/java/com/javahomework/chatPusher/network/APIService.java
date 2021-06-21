package com.javahomework.chatPusher.network;

import com.javahomework.chatPusher.model.Message;
import com.javahomework.chatPusher.model.response.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by kehinde on 9/25/17.
 */

public interface APIService {

    @POST("messages/{room}")
    Call<ServerResponse> sendMessage(@Path("room") String room,
                                     @Body Message message);
}
