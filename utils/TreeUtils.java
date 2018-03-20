package com.jie.utils;

import java.util.ArrayList;
import java.util.List;

import com.jie.model.extend.Tree;

/**
 * @ClassName: TreeUtils
 * @Description: 树节点类. <br/>
 * @author jie
 * @date 2018年03月10日
 */
public class TreeUtils {

    /**
     * 两层循环实现建树
     * @param trees 传入的树节点列表
     * @return
     */
    public static List<Tree> bulid(List<Tree> trees) {

        List<Tree> treeList = new ArrayList<Tree>();

        for (Tree tree : trees) {

            if ("0".equals(tree.getPid())) {
            	treeList.add(tree);
            }

            for (Tree it : trees) {
                if (it.getPid() == tree.getId()) {
                    if (tree.getChildren() == null) {
                        tree.setChildren(new ArrayList<Tree>());
                    }
                    tree.getChildren().add(it);
                }
            }
        }
        return treeList;
    }
    


    /**
     * 使用递归方法建树
     * @param trees
     * @return
     */
    public static List<Tree> buildByRecursive(List<Tree> trees) {
        List<Tree> treeList = new ArrayList<Tree>();
        for (Tree tree : trees) {
            if ("0".equals(tree.getPid())) {
            	treeList.add(findChildren(tree,trees));
            }
        }
        return treeList;
    }

    /**
     * 递归查找子节点
     * @param trees
     * @return
     */
    public static Tree findChildren(Tree tree,List<Tree> trees) {
        for (Tree it : trees) {
            if(tree.getId().equals(it.getPid())) {
                if (tree.getChildren() == null) {
                	tree.setChildren(new ArrayList<Tree>());
                }
                tree.getChildren().add(findChildren(it,trees));
            }
        }
        return tree;
    }


    /**
     * main
     * @param args
     */
    public static void main(String[] args) {

        Tree tree1 = new Tree("1","广州","0");
        Tree tree2 = new Tree("2","深圳","0");

        Tree tree3 = new Tree("3","天河区",tree1);
        Tree tree4 = new Tree("4","越秀区",tree1);
        Tree tree5 = new Tree("5","黄埔区",tree1);
        Tree tree6 = new Tree("6","石牌",tree3);
        Tree tree7 = new Tree("7","百脑汇",tree6);


        Tree tree8 = new Tree("8","南山区",tree2);
        Tree tree9 = new Tree("9","宝安区",tree2);
        Tree tree10 = new Tree("10","科技园",tree8);


        List<Tree> list = new ArrayList<Tree>();

        list.add(tree1);
        list.add(tree2);
        list.add(tree3);
        list.add(tree4);
        list.add(tree5);
        list.add(tree6);
        list.add(tree7);
        list.add(tree8);
        list.add(tree9);
        list.add(tree10);

        List<Tree> treeList1 = TreeUtils.bulid(list);
        System.out.println("treeList1=="+treeList1.toString());

        List<Tree> treeList2 = TreeUtils.buildByRecursive(list);
        System.out.println("treeList2=="+treeList2.toString());

    }

    
}
