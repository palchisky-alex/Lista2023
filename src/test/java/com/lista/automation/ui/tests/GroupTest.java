package com.lista.automation.ui.tests;

import static com.lista.automation.ui.core.utils.BasePage.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

import com.lista.automation.api.TestListener;
import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.api.pojo.group.GroupCreateRequest;
import com.lista.automation.api.pojo.group.GroupsGetResponse;
import com.lista.automation.ui.core.BaseTest;
import com.lista.automation.ui.pages.group.GroupPage;
import com.lista.automation.ui.pages.group.GroupsListPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

@Story("Verify groups")
@Epic("Group UI GRUD")
@Feature("Group")
@Listeners(TestListener.class)
public class GroupTest extends BaseTest {

    @Test
    @Description("UI: Rename client group")
    public void testGroupRename() throws Exception {


        GroupCreateRequest simpleGroup = generateGroup(true);
        api.group().create(simpleGroup, 201);


        calendar.routing()
                .toGroupsListPage()
                .longPressOnGroup(simpleGroup.getName())
                .configureGroup(GroupsListPage.ACTION.Rename);


        assertThat(api.group().getGroupsOfClient())
                .extracting(GroupsGetResponse::getName)
                .contains(generateGroup(false).getName());

    }

    @Test
    @Description("UI: Delete client group")
    public void testGroupDelete() throws Exception {


        GroupCreateRequest simpleGroup = generateGroup(true);
        int ID = api.group().create(simpleGroup, 201);


        calendar.routing()
                .toGroupsListPage()
                .longPressOnGroup(simpleGroup.getName())
                .configureGroup(GroupsListPage.ACTION.Delete);


        assertThat(api.group().getGroupsOfClient())
                .extracting(GroupsGetResponse::getId)
                .doesNotContain(ID);

    }

    @Test
    @Description("UI: Create client group")
    public void testGroupCreate() throws Exception {


        GroupCreateRequest simpleGroup = generateGroup(true);
        int ID = api.group().create(simpleGroup, 201);


        calendar.routing().toGroupsListPage()
                .addGroup(simpleGroup.getName())
                .returnToListGroups();


        assertThat(api.group().getGroupsOfClient())
                .extracting(GroupsGetResponse::getId)
                .contains(ID);


    }

    @Test
    @Description("UI: Add member into group")
    public void testAddMemberIntoGroup() throws Exception {

        ClientCreateRequest simpleClient = generateClient(true);
        String clientID = api.client().create(simpleClient, 201);


        api.client().find(simpleClient.getPhone().replaceAll("\\D+", ""));


        GroupCreateRequest simpleGroup = generateGroup(true);
        int ID = api.group().create(simpleGroup, 201);


        calendar.routing()
                .toGroupsListPage()
                .selectGroup(simpleGroup.getName())
                .initAddingMember()
                .searchMember(simpleClient.getName())
                .addToGroup();


        assertThat(api.group().getClientsOfGroup(ID, "clients", 200))
                .as("group contains ID of api client")
                .extracting(ClientGetResponse::getId)
                .contains(clientID);

    }

    @Test
    @Description("UI: Delete member from group")
    public void testDeleteMemberFromGroup() throws Exception {

        ClientCreateRequest simpleClient = generateClient(true);
        String clientID = api.client().create(simpleClient, 201);


        GroupCreateRequest simpleGroup = generateGroup(true);
        int groupID = api.group().create(simpleGroup, 201);


        api.group().putClientIntoGroup(groupID, "clients", clientID, 204);


        calendar.routing()
                .toGroupsListPage()
                .selectGroup(simpleGroup.getName())
                .initMenuForMember()
                .setMenuOptions(GroupPage.Menu.Opts.Select_All)
                .setMenuOptions(GroupPage.Menu.Opts.Cancel)
                .setMenuOptions(GroupPage.Menu.Opts.Select_All)
                .setMenuOptions(GroupPage.Menu.Opts.Delete);


        assertThat(api.group().getClientsOfGroup(groupID, "clients", 200))
                .as("group not contains ID of api client")
                .extracting(ClientGetResponse::getId)
                .doesNotContain(clientID);

    }

    @Test
    @Description("UI: Menu of default groups")
    public void testMenuDefaultGroups() throws Exception {

        List<String> defaultGroupsName =
                api.group().getAutomaticGroups()
                        .stream().map(GroupsGetResponse::getName)
                        .collect(Collectors.toList());

        assertThat(defaultGroupsName.size()).as("The Lista contains 8 default groups").isEqualTo(8);


        GroupsListPage groupsListPage = calendar.routing().toGroupsListPage();

        for (String name : defaultGroupsName) {
            groupsListPage
                    .longPressOnGroup(name)
                    .configureGroup(GroupsListPage.ACTION.ClosePopup);
        }

    }
}
