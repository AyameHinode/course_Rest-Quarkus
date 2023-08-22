package io.github.ayamehinode;

import io.quarkus.runtime.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;

import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "API Quarkus Social",
                version = "1.0",
                contact = @Contact(
                        name = "Ayame Hinode",
                        url = "http://quarkussocialcourse.com",
                        email = "ayamehinode@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)

public class QuarkusSocialApplication extends Application {
    protected QuarkusSocialApplication(boolean auxiliaryApplication) {
        super(auxiliaryApplication);
    }

    @Override
    protected void doStart(String[] args) {

    }

    @Override
    protected void doStop() {

    }

    @Override
    public String getName() {
        return null;
    }
}
