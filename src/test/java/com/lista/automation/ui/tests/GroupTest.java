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

import java.util.List;
import java.util.stream.Collectors;

@Epic("Group UI GRUD")
public class GroupTest extends BaseTest {

    @Test
    @Description("UI: Rename client group")
    public void testGroupRename() {
        step("UI: verify that client group can be rename", () -> {
            step("API: generate group", () -> {
                GroupCreateRequest simpleGroup = generateGroup(true);
                api.group.create(simpleGroup, 201);

                step("UI: rename group", () -> {
                    calendar.routing()
                            .toGroupsListPage()
                            .longPressOnGroup(simpleGroup.getName())
                            .configureGroup(GroupsListPage.ACTION.Rename);

                    step("API: search renamed group", () -> {
                        assertThat(api.group.getGroupsOfClient())
                                .extracting(GroupsGetResponse::getName)
                                .contains(generateGroup(false).getName());
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: Delete client group")
    public void testGroupDelete() {
        step("UI: verify that client group can be delete", () -> {

            step("API: generate group", () -> {
                GroupCreateRequest simpleGroup = generateGroup(true);
                int ID = api.group.create(simpleGroup, 201);

                step("UI: delete group", () -> {
                    calendar.routing()
                            .toGroupsListPage()
                            .longPressOnGroup(simpleGroup.getName())
                            .configureGroup(GroupsListPage.ACTION.Delete);

                    step("API: search deleted group", () -> {
                        assertThat(api.group.getGroupsOfClient())
                                .extracting(GroupsGetResponse::getId)
                                .doesNotContain(ID);
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: Create client group")
    public void testGroupCreate() {
        step("UI: verify that client group can be created", () -> {

            step("API: generate group", () -> {
                GroupCreateRequest simpleGroup = generateGroup(true);
                int ID = api.group.create(simpleGroup, 201);

                step("UI: add new group", () -> {
                    calendar.routing().toGroupsListPage()
                            .addGroup(simpleGroup.getName())
                            .returnToListGroups();

                    step("API: search deleted group", () -> {
                        assertThat(api.group.getGroupsOfClient())
                                .extracting(GroupsGetResponse::getId)
                                .contains(ID);
                    });
                });
            });
        });

    }

    @Test
    @Description("UI: Add member into group")
    public void testAddMemberIntoGroup() {
        step("API: generate simple client", () -> {
            ClientCreateRequest simpleClient = generateClient(true);
            String clientID = api.client.create(simpleClient, 201);

            step("API: check that the simple client has been created", () -> {
                api.client.find(simpleClient.getPhone().replaceAll("\\D+", ""), 200);
            });

            step("API: generate simple group", () -> {
                GroupCreateRequest simpleGroup = generateGroup(true);
                int ID = api.group.create(simpleGroup, 201);

                step("UI: add member into group", () -> {
                    calendar.routing()
                            .toGroupsListPage()
                            .selectGroup(simpleGroup.getName())
                            .initAddingMember()
                            .searchMember(simpleClient.getName())
                            .addToGroup();

                    step("API: get group with member", () -> {
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
    @Description("UI: Delete member from group")
    public void testDeleteMemberFromGroup() {
        step("API: generate client", () -> {
            ClientCreateRequest simpleClient = generateClient(true);
            String clientID = api.client.create(simpleClient, 201);

            step("API: generate group", () -> {
                GroupCreateRequest simpleGroup = generateGroup(true);
                int groupID = api.group.create(simpleGroup, 201);

                step("API: put member into group", () -> {
                    api.group.putClientIntoGroup(groupID, "clients", clientID, 204);

                    step("UI: delete member from group", () -> {
                        calendar.routing()
                                .toGroupsListPage()
                                .selectGroup(simpleGroup.getName())
                                .initMenuForMember()
                                .setMenuOptions(GroupPage.Menu.Opts.Select_All)
                                .setMenuOptions(GroupPage.Menu.Opts.Cancel)
                                .setMenuOptions(GroupPage.Menu.Opts.Select_All)
                                .setMenuOptions(GroupPage.Menu.Opts.Delete);

                        step("API: get group with member", () -> {
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

    @Test
    @Description("UI: Menu of default groups")
    public void testMenuDefaultGroups() {
        step("API: get all Lista's default groups", () -> {
            List<String> defaultGroupsName =
                    api.group.getAutomaticGroups()
                            .stream().map(GroupsGetResponse::getName)
                            .collect(Collectors.toList());

            assertThat(defaultGroupsName.size()).as("The Lista contains 8 default groups").isEqualTo(8);

            step("UI: init menu of all default groups one by one", () -> {
                GroupsListPage groupsListPage = calendar.routing().toGroupsListPage();

                for (String name : defaultGroupsName) {
                    groupsListPage
                            .longPressOnGroup(name)
                            .configureGroup(GroupsListPage.ACTION.ClosePopup);
                }

            });
        });
    }
}
