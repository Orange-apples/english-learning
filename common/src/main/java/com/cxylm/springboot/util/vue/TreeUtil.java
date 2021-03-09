package com.cxylm.springboot.util.vue;

import com.cxylm.springboot.systemdto.RouterMeta;
import com.cxylm.springboot.systemdto.VueRouter;
import com.cxylm.springboot.systemdto.VueTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeUtil {
    protected TreeUtil() {

    }

    private final static long TOP_NODE_ID = 0;

    /**
     * 用于构建菜单或部门树
     *
     * @param nodes nodes
     * @param <T>   <T>
     * @return <T> VueTree<T>
     */
    public static <T> VueTree<T> buildVueTree(List<VueTree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<VueTree<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            Long pid = node.getPid();
            if (pid == null || TOP_NODE_ID == pid) {
                topNodes.add(node);
                return;
            }
            for (VueTree<T> tree : nodes) {
                Long id = tree.getId();
                if (id != null && id.equals(pid)) {
                    if (tree.getChildren() == null) {
                        tree.initChildren();
                    }
                    tree.getChildren().add(tree);
                    tree.setHasParent(true);
                    tree.setHasChildren(true);
                    tree.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty())
                topNodes.add(node);
        });

        VueTree<T> root = new VueTree<>();
        root.setId(0L);
        root.setPid(0L);
        root.setHasParent(false);
        root.setHasChildren(true);
        root.setChildren(topNodes);
        return root;
    }

    public static  <T> ArrayList<VueRouter<T>> buildVueRouter(List<VueRouter<T>> routes) {
        if (routes == null) {
            return null;
        }
        ArrayList<VueRouter<T>> topRoutes = new ArrayList<>();
        VueRouter<T> router = new VueRouter<>();

        routes.forEach(route -> {
            route.setMeta(new RouterMeta(false, true, route.getName(), route.getIcon()));
            Long parentId = route.getPid();
            if (parentId == null || TOP_NODE_ID == parentId) {
                topRoutes.add(route);
                return;
            }
            for (VueRouter<T> parent : routes) {
                Long id = parent.getId();
                if (id != null && id.equals(parentId)) {
                    if (parent.getChildren() == null)
                        parent.initChildren();
                    parent.getChildren().add(route);
                    parent.setHasChildren(true);
                    route.setHasParent(true);
                    parent.setHasParent(true);
                    return;
                }
            }
        });

        router = new VueRouter<>();
        router.setPath("/http-website");
        router.setName("访问官网");
        router.setComponent("Layout");

        VueRouter<T> webChildren = new VueRouter<>();
        webChildren.setMeta(new RouterMeta(false, false, "访问官网", "link"));
        webChildren.setPath("https://btcyingyu.com/");
        router.setChildren(Collections.singletonList(webChildren));
        topRoutes.add(router);

        // Add 404 page
        router = new VueRouter<>();
        router.setPath("*");
        router.setRedirect("/404");
        router.setHidden(true);
        topRoutes.add(router);

        return topRoutes;
    }
}
