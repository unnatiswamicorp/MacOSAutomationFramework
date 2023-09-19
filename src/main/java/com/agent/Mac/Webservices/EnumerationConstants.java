package com.agent.Mac.Webservices;

public class EnumerationConstants {
	public enum CustomerType {
		NORMAL, SPS, MAM, MASTERADMIN, MDMPERSONA,USER,
	}

	public enum AdminType {
		SERVICE;
	}
	public enum DeviceDetails{
		AndroidDeviceUserName,iOSDeviceUserName,iOSDeviceEmail,AndroidDeviceEmail,iOSwifiMacAddress,AndroidwifiMacAddress,

	}

	public enum FeedType {
		DEVICE, USER, DOCUMENT, APP;
	}

	public enum SearchType {
		GLOBAL, QUICK
	}

	public enum MethodType {
		POST, GET, PUT,DELETE
	}

	public enum WebServices {
		Authentication, CreateCustomerAccount, BasicSearch, UploadAppleMDMCert, AddITunesApp, AddIOSEnterpriseApp,AddiTunesAppStoreAppV1,AddIOSEnterpriseAppPlus,DeleteApp,AddPlayApp,Policies, SearchUser, GetWatchLists, SearchInstalledApps, GetDeviceGroups, ConvertToCustomer,
		SetCustomerConfig, CreateAdministrator, GetCustomerConfig, CheckAccountAvailability, ExpireAccount, EnrollDevice, ExtendAccount,MdNetworkInformation,CellularDataUsage,

		CreatePartnerAccount, SetPartnerAccountConfig, GetPartnerAccountConfig, GetSignedCSR,SearchApps,DistributeApp,GetAppDistributionByDevice,SearchDistributions,AuthenticateAdministrator, SecurityApplications,

		ApproveDeviceMessagingSystem,GetSummaryAttributes,GetCoreBulk,CheckAdminAccountAvailability, GetDeviceEnrollmentSettings, ConfigureDeviceEnrollSettings,GetAppDetails,AddAndroidEnterpriseApp,LockDevice,LocateDevice,
		WipeDevice, SelectiveWipeDevice, SendMessage, GetCore, HardwareInventory, SoftwareInstalled, MdSecurityCompliance,BulkSummary,PackageDistributionHistory, Identity,UpgradeApp,








		RevokeSelectiveWipe,changeRuleSet, CancelPendingWipe, RemoveDevice,RefreshDeviceInformation,CheckActionStatus, SearchActionHistory, SetCustomAtribute, DeviceActions, ResetDevicePasscode,StopAppDistribution,ManageDeviceEnrollments,CreateDeviceEnrollment,SendMessageToDevice,GetDeviceLocation,DeviceAction,SearchByDeviceGroup,
		DeviceDataView, SearchByWatchList,ChangeDevicePolicy, UpdateProvisioningProfile,AppUploadRequestStatus, authversion2, refreshToken, ChangePersonaPolicy, AddAndroidEntAppPlus, UpgradeAppPlus, AddiTunesAppV2, AddGooglePlayAppV2, SearchDistributionV2,ChangeEPSPolicy,
		SetCustomAttributes, mdmServerInfo, mdmDeviceQueryWithAttributes, mdmDeviceQuery, mdmDeviceQueryBatch, mdmAction, mdmMessage, LocationHistory, GetGroups, HideDevice, setNewPasswordForUser, ResetPasswordForUser, UserCustomAttributeValues, BasicSearch2,
		searchByGroup, groupLevelDistribution, AddLocalUserAccount, AddGroupsToUserAccount, GetUserGroups, RemoveGroupsFromUserAccount, PublishDevicePolicy, GetDevicePolicy, EditDevicePolicy, CreateDevicePolicy, AppsVulnerable







	}

	public enum DeviceType {
		ANDROID, IOS, WINDOWSPHONE
	}
}