package com.podio.org;

import java.net.URISyntaxException;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

import com.podio.APIFactoryProvider;
import com.podio.common.Role;
import com.podio.space.Space;
import com.podio.space.SpaceMini;

public class OrgAPITest {

	private OrgAPI getAPI() {
		return APIFactoryProvider.getDefault().getAPI(OrgAPI.class);
	}

	@Test
	public void createOrganization() {
		OrganizationCreateResponse response = getAPI().createOrganization(
				new OrganizationCreate("CubiTech2", null));

		Assert.assertTrue(response.getId() > 1);
		Assert.assertEquals(response.getUrl(), "https://cubitech2.podio.com/");
	}

	@Test
	public void getOrganization() {
		Organization org = getAPI().getOrganization(1);

		Assert.assertEquals(org.getId(), 1);
		Assert.assertEquals(org.getName(), "Hoist");
		Assert.assertEquals(org.getUrl(), "https://hoist.podio.com/");
		Assert.assertEquals(org.getLogo().intValue(), 10);
		Assert.assertEquals(org.getStatus(), OrganizationStatus.ACTIVE);
		Assert.assertEquals(org.getUserLimit(), 50);
		Assert.assertEquals(org.getCreatedBy().getUserId().intValue(), 1);
		Assert.assertEquals(org.getCreatedOn(), new DateTime(2010, 8, 6, 23, 1,
				0, 0, DateTimeZone.UTC));
	}

	@Test
	public void updateOrganization() {
		getAPI().updateOrganization(1, new OrganizationCreate("New name", null));
	}

	@Test
	public void getOrganizations() throws URISyntaxException {
		List<OrganizationWithSpaces> organizations = getAPI()
				.getOrganizations();
		Assert.assertEquals(organizations.size(), 3);

		OrganizationWithSpaces org = organizations.get(0);
		Assert.assertEquals(org.getId(), 1);
		Assert.assertEquals(org.getName(), "Hoist");
		List<SpaceMini> spaces = org.getSpaces();
		Assert.assertEquals(spaces.size(), 3);
		SpaceMini space = spaces.get(0);
		Assert.assertEquals(space.getId(), 1);
		Assert.assertEquals(space.getName(), "API");
	}

	@Test
	public void getOrganizationByURL() {
		OrganizationMini org = getAPI().getOrganizationByURL(
				"https://hoist.podio.com/");

		Assert.assertEquals(org.getId(), 1);
	}

	@Test
	public void getOrganizationStatistics() {
		OrganizationStatistics stats = getAPI().getOrganizationStatistics(1);

		Assert.assertEquals(stats.getId(), 1);
		Assert.assertEquals(stats.getAppsCount(), 3);
		Assert.assertEquals(stats.getAvailableInvitations(), 0);
		Assert.assertEquals(stats.getItemsCount(), 7);
		Assert.assertNotNull(stats.getLastActivityOn());
		Assert.assertEquals(stats.getSpacesCount(), 5);
		Assert.assertEquals(stats.getUsersCount(), 0);
	}

	@Test
	public void getSharedOrganizations() {
		List<OrganizationWithSpaces> organizations = getAPI()
				.getSharedOrganizations(2);

		Assert.assertEquals(organizations.size(), 3);
		Assert.assertEquals(organizations.get(0).getId(), 1);
		Assert.assertEquals(organizations.get(0).getSpaces().size(), 3);
		Assert.assertEquals(organizations.get(0).getSpaces().get(0).getId(), 1);
	}

	@Test
	public void getSpaceByURL() {
		Space space = getAPI().getSpaceByURL(1, "api");

		Assert.assertEquals(space.getId(), 1);
	}

	@Test
	public void getSpaces() {
		List<Space> spaces = getAPI().getSpaces(1);

		Assert.assertEquals(spaces.size(), 3);
		Assert.assertEquals(spaces.get(0).getId(), 1);
	}

	@Test
	public void getMembers() {
		List<OrganizationMember> members = getAPI().getMembers(1);

		Assert.assertEquals(members.size(), 4);
		OrganizationMember member = members.get(0);
		Assert.assertEquals(member.getUser().getId(), 4);
		Assert.assertEquals(member.getProfile().getProfileId(), 4);
		Assert.assertEquals(member.getRole(), Role.REGULAR);
		Assert.assertEquals(member.getSpaceMemberships(), 1);
	}
}
