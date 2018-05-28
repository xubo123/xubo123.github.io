package com.hxy.core.role.action;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.base.entity.Tree;
import com.hxy.core.resource.entity.Resource;
import com.hxy.core.role.entity.Role;
import com.hxy.core.role.service.RoleService;

@Namespace("/role")
@Action(value = "roleAction")
public class RoleAction extends AdminBaseAction
{
	private static final Logger logger = Logger.getLogger(RoleAction.class);

	private Role role = new Role();

	@Autowired
	private RoleService roleService;

	public void save()
	{
		Message message = new Message();
		try
		{
			roleService.save(role);
			message.setMsg("保存成功");
			message.setSuccess(true);
		} catch (Exception e)
		{
			logger.error(e, e);
			message.setMsg("保存失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void getList()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String roleName = getRequest().getParameter("roleName");
		map.put("page", page);
		map.put("rows", rows);
		map.put("roleName", roleName);
		super.writeJson(roleService.dataGrid(map));
	}

	public void getById()
	{
		super.writeJson(roleService.getById(role.getRoleId()));
	}
	
	public void doNotNeedSecurity_getById()
	{
		super.writeJson(roleService.getById(role.getRoleId()));
	}

	public void update()
	{
		Message message = new Message();
		try
		{
			roleService.update(role);
			message.setMsg("保存成功");
			message.setSuccess(true);
		} catch (Exception e)
		{
			logger.error(e, e);
			message.setMsg("保存失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void delete()
	{
		Message message = new Message();
		try
		{
			roleService.delete(id);
			message.setMsg("删除成功");
			message.setSuccess(true);
		} catch (Exception e)
		{
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}
	
	public void doNotNeedSecurity_getNoAdmin(){
		super.writeJson(roleService.selectAllNoAdmin());
	}
	
	public void doNotNeedSecurity_getxNoAdmin(){
		super.writeJson(roleService.selectxAllNoAdmin());
	}

	public void doNotNeedSecurity_getHasTree()
	{
		role = roleService.selectResource(id);
		List<Resource> list = role.getList();
		super.writeJson(list);
	}

	public void grant()
	{
		Message message = new Message();
		try
		{
			roleService.updateGrant(ids, id);
			message.setMsg("授权成功");
			message.setSuccess(true);
		} catch (Exception e)
		{
			logger.error(e, e);
			message.setMsg("授权失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void doNotNeedSecurity_getGrantTree()
	{
		List<Role> list = roleService.selectAll();
		List<Tree> tree = new ArrayList<Tree>();
		for (Role role : list)
		{
			if(role.getFlag()==0&&role.getSystemAdmin()==0){
				Tree node = new Tree();
				node.setId(role.getRoleId());
				node.setText(role.getRoleName());
				tree.add(node);
			}
		}
		super.writeJson(tree);
	}
	
	public void doNotNeedSecurity_getxGrantTree()
	{
		List<Role> list = roleService.selectAll();
		List<Tree> tree = new ArrayList<Tree>();
		for (Role role : list)
		{
			if(role.getFlag()==1){
				Tree node = new Tree();
				node.setId(role.getRoleId());
				node.setText(role.getRoleName());
				tree.add(node);
			}
		}
		super.writeJson(tree);
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}

}
