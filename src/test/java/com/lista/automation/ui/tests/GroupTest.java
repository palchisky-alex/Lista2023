package com.lista.automation.ui.tests;

import static com.lista.automation.ui.core.utils.BasePage.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.api.pojo.group.GroupCreateRequest;
import com.lista.automation.api.pojo.group.GroupsGetResponse;
import com.lista.automation.ui.core.BaseTest;
import com.lista.automation.ui.pages.group.GroupPage;
import com.lista.automation.ui.pages.group.GroupsListPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.Test;

@Epic("Group UI GRUD")
public class GroupTest extends BaseTest {

    @Test
    @Description("Rename client group")
    public void testGroupRename() {
        step("Verify that client group can be rename via UI", () -> {
            step("generate api group", () -> {
                GroupCreateRequest simpleGroup = generateGroup(true);
                api.group.create(simpleGroup, 201);

                step("rename group via UI", () -> {
                    calendar.routing()
                            .toGroupsListPage()
                            .configureGroup(simpleGroup.getName(), GroupsListPage.ACTION.Rename);

                    step("search renamed group via api", () -> {
                        assertThat(api.group.getGroupsOfClient())
                                .extracting(GroupsGetResponse::getName)
                                .contains(generateGroup(false).getName());
                    });
                });
            });
        });
    }

    @Test
    @Description("Delete client group")
    public void testGroupDelete() {
        step("Verify that client group can be delete via UI", () -> {

            step("generate api group", () -> {
                GroupCreateRequest simpleGroup = generateGroup(true);
                int ID = api.group.create(simpleGroup, 201);

                step("delete group via UI", () -> {
                    calendar.routing()
                            .toGroupsListPage()
                            .configureGroup(simpleGroup.getName(), GroupsListPage.ACTION.Delete);

                    step("search deleted group via api", () -> {
                        assertThat(api.group.getGroupsOfClient())
                                .extracting(GroupsGetResponse::getId)
                                .doesNotContain(ID);
                    });
                });
            });
        });
    }

    @Test
    @Description("Create client group")
    public void testGroupCreate() {
        step("Verify that client group can be created via UI", () -> {

            step("generate api group", () -> {
                GroupCreateRequest simpleGroup = generateGroup(true);
                int ID = api.group.create(simpleGroup, 201);

                step("add new group via UI", () -> {
                    calendar.routing().toGroupsListPage()
                            .addGroup(simpleGroup.getName())
                            .returnToListGroups();

                    step("search deleted group via api", () -> {
                        assertThat(api.group.getGroupsOfClient())
                                .extracting(GroupsGetResponse::getId)
                                .contains(ID);
                    });
                });
            });
        });

    }

    @Test
    @Description("Add member into group")
    public void testAddMemberIntoGroup() {
        step("generate simple client via api", () -> {
            ClientCreateRequest simpleClient = generateClient(true);
            String clientID = api.client.create(simpleClient, 201);

            step("check via api that the simple client has been created", () -> {
                api.client.find(simpleClient.getPhone().replaceAll("\\D+", ""), 200);
            });

            step("generate simple group via api", () -> {
                GroupCreateRequest simpleGroup = generateGroup(true);
                int ID = api.group.create(simpleGroup, 201);

                step("add member into group via UI", () -> {
                    calendar.routing()
                            .toGroupsListPage()
                            .selectGroup(simpleGroup.getName())
                            .initAddingMember()
                            .searchMember(simpleClient.getName())
                            .addToGroup();

                    step("get group with member via api", () -> {
                        assertThat(api.group.getClientsOfGroup(ID, "clients", 200))
                                .as("group contains ID of api client")
                                .extracting(ClientGetResponse::getId)
                                .contains(clientID);
                    });
                });
            });
        });
    }

    @Test
    public void testDeleteMemberFromGroup() {
        step("generate client via api", () -> {
            ClientCreateRequest simpleClient = generateClient(true);
            String clientID = api.client.create(simpleClient, 201);

            step("generate group via api", () -> {
                GroupCreateRequest simpleGroup = generateGroup(true);
                int groupID = api.group.create(simpleGroup, 201);

                step("put member into group via api", () -> {
                    api.group.putClientToGroup(groupID, "clients", clientID, 204);

                    step("delete member from group via UI", () -> {
                        calendar.routing()
                                .toGroupsListPage()
                                .selectGroup(simpleGroup.getName())
                                .initMenuForMember()
                                .setMenuOptions(GroupPage.Menu.Opts.Select_All);

                        step("get group with member via api", () -> {
                            assertThat(api.group.getClientsOfGroup(groupID, "clients", 200))
                                    .as("group not contains ID of api client")
                                    .extracting(ClientGetResponse::getId)
                                    .doesNotContain(clientID);
                        });
                    });
                });
            });
        });
    }
}
