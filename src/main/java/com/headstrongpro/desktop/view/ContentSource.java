package com.headstrongpro.desktop.view;

/**
 * Content Sources
 */
public enum ContentSource {


    ABOUT("About", "/layout/about.fxml", null),

    RESOURCES("Resources", "/layout/resources/resourcesContentPane.fxml", "/layout/resources/resourcesContextDefault.fxml"),
    RESOURCES_IMAGE("Image resources", "/layout/resources/resourcesContentPane.fxml", "/layout/resources/resourcesImagePane.fxml"),
    RESOURCES_AUDIO("Audio resources", "/layout/resources/resourcesContentPane.fxml", "/layout/resources/resourcesAudioPane.fxml"),
    RESOURCES_TEXT("Text resources", "/layout/resources/resourcesContentPane.fxml", "/layout/resources/resourcesTextPane.fxml"),
    RESOURCES_NEW_TEXT("New text resource", "/layout/resources/resourcesContentPane.fxml", "/layout/resources/resourcesNewTextPane.fxml"),
    RESOURCES_NEW_FILE("New resource", "/layout/resources/resourcesContentPane.fxml", "/layout/resources/resourcesNewFilePane.fxml"),

    COMPANIES("Companies", "/layout/companies/companiesContentPane.fxml", "/layout/companies/companiesContextPane.fxml"),
    COMPANIES_NEW("New company", "/layout/companies/companiesContentPane.fxml", "/layout/companies/companiesNewPane.fxml"),

    CLIENTS("Clients", "/layout/clients/clientsContentPane.fxml", "/layout/clients/clientsContextPane.fxml"),
    SUBSCRIPTIONS("Subscriptions", "/layout/subscriptions/subscriptionsContentPane.fxml", "/layout/subscriptions/subscriptionsContextPane.fxml"),
    DASHBOARD("Dashboard", "/layout/dashboardPane.fxml", null),
    DEPARTMENTS("Departments", "/layout/departments/departmentsContentPane.fxml", "/layout/departments/departmentsContextPane.fxml");
    private String name, contentView, contextView;

    ContentSource(String name, String contentView, String contextView) {
        this.name = name;
        this.contentView = contentView;
        this.contextView = contextView;
    }

    public String getName() {
        return name;
    }

    public String getContentView() {
        return contentView;
    }

    public String getContextView() {
        return contextView;
    }
}
