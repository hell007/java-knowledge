

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.beetl.core.GroupTemplate;

/**
 * 
shrio 提供有jsp 标签，供在jsp 里使用，如果想在beetl中使用，有俩种方法，
一是beetl支持集成jsp页面，所以你可以在在jsp里使用shrio标签
另外，beetl 使用自定义函数写了shiro tag功能，你可以像使用shiro标签那样使用shiro
*/
/*
public class BeetlConfiguration extends BeetlGroupUtilConfiguration {
    @Override
    protected void initOther() {
        groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
    }
}

你可以在模板里直接调用，譬如
<% if(shiro.isGuest()) {%>
	.....
<%}%>

${ shiro.isGuest() }

map
${shiro.principal({property:'name' \})}

*/
public class ShiroExt {
	
	 private static final String NAMES_DELIMETER = ",";
	 
	/**
     * 获取当前 Subject
     * @return Subject
     */
	protected Subject getSubject() {
		return SecurityUtils.getSubject();
	}
	
	
	/**
     * 验证当前用户是否为“访客”，即未认证（包含未记住）的用户。用user搭配使用
     * @return 访客：true，否则false
     */
	public boolean isGuest() {
		return !isUser();
	}

	
	/**
     * 认证通过或已记住的用户。与guset搭配使用。
     * @return 用户：true，否则 false
     */
	public boolean isUser() {
		return getSubject() != null && getSubject().getPrincipal() != null;
	}
	
	
	/**
     * 已认证通过的用户。不包含已记住的用户，这是与user标签的区别所在。与notAuthenticated搭配使用
     * @return 通过身份验证：true，否则false
     */
	public boolean isAuthenticated() {
		return getSubject() != null && getSubject().isAuthenticated();
	}
	
	
	/**
     * 未认证通过用户，与authenticated标签相对应。与guest标签的区别是，该标签包含已记住用户。。
     * @return 没有通过身份验证：true，否则false
     */
	public boolean isNotAuthenticated() {
		return !isAuthenticated();
	}
	
	
	/**
     * 验证当前用户是否拥有指定权限,使用时与lacksPermission 搭配使用
     * @param permission 权限名
     * @return 拥有权限：true，否则false
     */
	public boolean hasPermission(String permission) {
		return getSubject() != null && permission != null
                && permission.length() > 0
                && getSubject().isPermitted(permission);
	}
	

	/**
     * 与hasPermission标签逻辑相反，当前用户没有制定权限时，验证通过。
     * @param permission 权限名
     * @return 拥有权限：true，否则false
     */
    public boolean lacksPermission(String permission) {
        return !hasPermission(permission);
    }
    
    
    /**
     * 验证当前用户是否属于该角色？,使用时与lacksRole 搭配使用
     * @param roleName 角色名
     * @return 属于该角色：true，否则false
     */
    public boolean hasRole(String roleName) {
        return getSubject() != null && roleName != null
                && roleName.length() > 0 && getSubject().hasRole(roleName);
    }


    /**
     * 与hasRole标签逻辑相反，当用户不属于该角色时验证通过。
     *
     * @param roleName 角色名
     * @return 不属于该角色：true，否则false
     */
    public boolean lacksRole(String roleName) {
        return !hasRole(roleName);
    }

    
    /**
     * 验证当前用户是否属于以下任意一个角色。
     * @param roleNames 角色列表
     * @return 属于:true,否则false
     */
    public boolean hasAnyRoles(String roleNames) {
        boolean hasAnyRole = false;
        Subject subject = getSubject();
        if (subject != null && roleNames != null && roleNames.length() > 0) {
            for (String role : roleNames.split(NAMES_DELIMETER)) {
                if (subject.hasRole(role.trim())) {
                    hasAnyRole = true;
                    break;
                }
            }
        }
        return hasAnyRole;
    }
    
    
    /**
     * 验证当前用户是否属于以下所有角色。
     * @param roleNames 角色列表
     * @return 属于:true,否则false
     */
    public boolean hasAllRoles(String roleNames) {
        boolean hasAllRole = true;
        Subject subject = getSubject();
        if (subject != null && roleNames != null && roleNames.length() > 0) {
            for (String role : roleNames.split(NAMES_DELIMETER)) {
                if (!subject.hasRole(role.trim())) {
                    hasAllRole = false;
                    break;
                }
            }
        }
        return hasAllRole;
    }
    
    
    /**
     * 输出当前用户信息，通常为登录帐号信息。
     * @return 当前用户信息
     */
    public String principal() {
        if (getSubject() != null) {
            Object principal = getSubject().getPrincipal();
            return principal.toString();
        }
        return "";
    }
    
    
	/**
	 * The principal tag
	 * 
	 * @param map
	 * @return
	 */
	public String principal(Map map) {
		String strValue = null;
		if (getSubject() != null) {

			// Get the principal to print out
			Object principal;
			String type = map != null ? (String) map.get("type") : null;
			if (type == null) {
				principal = getSubject().getPrincipal();
			} else {
				principal = getPrincipalFromClassName(type);
			}
			String property = map != null ? (String) map.get("property") : null;
			// Get the string value of the principal
			if (principal != null) {
				if (property == null) {
					strValue = principal.toString();
				} else {
					strValue = getPrincipalProperty(principal, property);
				}
			}

		}

		if (strValue != null) {
			return strValue;
		} else {
			return null;
		}
	}

	

	

	@SuppressWarnings({ "unchecked" })
	private Object getPrincipalFromClassName(String type) {
		Object principal = null;

		try {
			Class cls = Class.forName(type);
			principal = getSubject().getPrincipals().oneByType(cls);
		} catch (ClassNotFoundException e) {

		}
		return principal;
	}

	
	private String getPrincipalProperty(Object principal, String property) {
		String strValue = null;

		try {
			BeanInfo bi = Introspector.getBeanInfo(principal.getClass());

			// Loop through the properties to get the string value of the
			// specified property
			boolean foundProperty = false;
			for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
				if (pd.getName().equals(property)) {
					Object value = pd.getReadMethod().invoke(principal,
							(Object[]) null);
					strValue = String.valueOf(value);
					foundProperty = true;
					break;
				}
			}

			if (!foundProperty) {
				final String message = "Property [" + property
						+ "] not found in principal of type ["
						+ principal.getClass().getName() + "]";

				throw new RuntimeException(message);
			}

		} catch (Exception e) {
			final String message = "Error reading property [" + property
					+ "] from principal of type ["
					+ principal.getClass().getName() + "]";

			throw new RuntimeException(message, e);
		}

		return strValue;
	}
	
	

	public static void main(String[] args) {
		GroupTemplate gt = new GroupTemplate();
		gt.registerFunctionPackage("shiro", new ShiroExt());
	}

}
