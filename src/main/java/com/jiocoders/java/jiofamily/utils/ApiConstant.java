package com.jiocoders.java.jiofamily.utils;

public class ApiConstant {

    /**
     * ----------------- ALL SERVICES -----------------
     */
    public static final String ADMIN_SERVICE = "adminService";
    public static final String LOGIN_SERVICE = "loginService";
    public static final String DEPARTMENT_SERVICE = "departmentService";
    public static final String WAREHOUSE_SERVICE = "warehouseService";
    public static final String LOCATION_SERVICE = "locationService";
    public static final String TEST_SERVICE = "testService";
    public static final String ITEM_SERVICE = "itemService";
    public static final String ITEM_GROUP_SERVICE = "itemGroupService";
    public static final String ITEM_TYPE_SERVICE = "itemTypeService";
    public static final String ITEM_UNIT_SERVICE = "itemUnitService";
    public static final String USER_ROLE_SERVICE = "UserRoleService";
    public static final String PERMISSION_SERVICE = "PermissionService";
    public static final String PROGRESS_STATUS_SERVICE = "ProgressStatusService";
    public static final String SUPPLIER_SERVICE = "supplierService";
    public static final String SUPPLIER_ITEM_MAP_SERVICE = "supplierItemMapService";
    public static final String WAREHOUSE_ITEM_MAP_SERVICE = "warehouseItemMapService";
    public static final String TEST_ITEM_MAP_SERVICE = "testItemMapService";

    /**
     * -----------------END POINTS------------------
     */
    // LOGIN CONTROLLER
    public static final String WELCOME = "/welcome";
    public static final String USER_LOGIN = "/user-login";

    // ADMIN CONTROLLER
    public static final String USER_ADD = "/user-add";
    public static final String USER_UPDATE = "/user-update";
    public static final String USER_LIST = "/user-list";
    public static final String USER_VALIDATE = "/user-validate";
    public static final String USER_ACTIVE = "/user-active";
    public static final String RESET_PASSWORD = "/reset-password";

    /**
     * ----------------- CONTROLLERS -----------------
     */

    /**
     * COMMON CONTROLLER
     */
    public static final String API = "/api/v1";

    /**
     * DEPARTMENT CONTROLLER
     */
    public static final String DEPARTMENT_ADD = "/department-add";
    public static final String DEPARTMENT_UPDATE = "/department-update";
    public static final String DEPARTMENT_LIST = "/department-list";

    /**
     * WAREHOUSE CONTROLLER
     */
    public static final String WAREHOUSE_ADD = "/warehouse-add";
    public static final String WAREHOUSE_UPDATE = "/warehouse-update";
    public static final String WAREHOUSE_REMOVE = "/warehouse-remove";
    public static final String WAREHOUSE_LIST = "/warehouse-list";

    /**
     * LOCATION CONTROLLER
     */
    public static final String LOCATION_ADD = "/location-add";
    public static final String LOCATION_UPDATE = "/location-update";
    public static final String LOCATION_LIST = "/location-list";
    /**
     * USER_ROLE CONTROLLER
     */
    public static final String USER_ROLE_ADD = "/user-role-add";
    public static final String USER_ROLE_UPDATE = "/user-role-update";
    public static final String USER_ROLE_LIST = "/user-role-list";
    /**
     * Common CONTROLLER
     */
    public static final String ADD = "/add";
    public static final String UPDATE = "/update";
    public static final String LIST = "/list";
    public static final String REMOVE = "/remove";
    public static final String LIST_DATE_RANGE = "/list-date-range";
    public static final String DETAILS = "/details";
    public static final String CHANGE_STATUS_DRAFT_SUBMITTED = "/change-status-draft-submitted";
    public static final String CHANGE_STATUS_SUBMITTED_APPROVED = "/change-status-submitted-approved";
    public static final String CHANGE_STATUS_DRAFT_APPROVED = "/change-status-draft-approved";

    /**
     * Supplier-Item map CONTROLLER
     */
    public static final String LIST_OF_UNIQUE_SUPPLIER_FROM_ITEMS = "/list-unique-supplier-from-items";
    public static final String LIST_OF_UNIQUE_ITEM_FROM_SUPPLIER_PR = "/list-unique-items-from-supplier-pr";
    public static final String LIST_OF_PR_BY_SUPPLIER = "/list-pr-by-supplier";

    /**
     * Warehouse-Item map CONTROLLER
     */
    public static final String LIST_OF_UNIQUE_WAREHOUSE_FROM_ITEMS = "/list-unique-warehouse-from-items";

    /**
     * REQUEST CODE
     */
    public static final int INVALID_REQUEST_CODE = -1;
    // public static final int BAD_REQUEST_CODE = 400;
    // public static final int BAD_TOKEN_CODE = 401;
    // public static final int FORBIDDEN_CODE = 403;
    // public static final int SUCCESS_INSERT_CODE = 201;
    public static final int SUCCESS_CODE = 1;
    // public static final int SERVER_ERROR_CODE = 500;

    /**
     * OTHER
     */
    public static final String APPLICATION_JSON = "application/json";

}
