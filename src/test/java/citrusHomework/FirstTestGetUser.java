package citrusHomework;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

import citrusHomework.pojo.user.Support;
import citrusHomework.pojo.user.UserDTO;
import citrusHomework.pojo.user.UserData;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.TestNGCitrusSupport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

public class FirstTestGetUser extends TestNGCitrusSupport {

  private TestContext context;

  @Test(description = "Получение информации о пользователе")
  @CitrusTest
  public void getTestActions() {
    this.context = citrus.getCitrusContext().createTestContext();

    context.setVariable("newVar", "varInContext");

    $(echo("Property in context 'newVar' = " + context.getVariable("newVar")));
    $(echo("Property in citrus.properties 'userId' = " + context.getVariable("userId")));
    $(echo("Property in citrus.properties 'userId' = ${userId}"));

    variable("now", "citrus:currentDate()");
    $(echo("Today is = ${now}"));

    $(http()
        .client("restClient")
        .send()
        .get("users/${userId}")
    );

    $(http()
            .client("restClient")
            .receive()
            .response(HttpStatus.OK)
            .message()
//        .body(new ClassPathResource("json/user.json"))
            .body(new ObjectMappingPayloadBuilder(getUserJson(), "objectMapper"))
    );
  }

  public UserDTO getUserJson() {
    UserDTO user = UserDTO
        .builder()
        .data(UserData.builder()
            .id(Integer.parseInt(context.getVariable("userId")))
            .email("janet.weaver@reqres.in")
            .firstName("Janet")
            .lastName("Weaver")
            .avatar("https://reqres.in/img/faces/2-image.jpg")
            .build())
        .support(Support.builder()
            .url("https://reqres.in/#support-heading")
            .text("To keep ReqRes free, contributions towards server costs are appreciated!")
            .build())
        .build();

    return user;
  }
}
