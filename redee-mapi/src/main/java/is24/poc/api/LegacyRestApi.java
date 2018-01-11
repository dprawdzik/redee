package is24.poc.api;

import com.scout24.redee.extraction.model.LegacyExposeResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface LegacyRestApi {

  String ENDPOINT = "https://rest.immobilienscout24.de/restapi/api/search/v1.0/";

  @GET("expose/{exposeId}")
  @Headers("Accept: application/json")
  Call<LegacyExposeResponse> getExpose(@Path("exposeId") String exposeId);

}
