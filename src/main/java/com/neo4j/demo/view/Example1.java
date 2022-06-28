package com.neo4j.demo.view;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.service.GraphService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.Arrays;


@PageTitle("Example1")
@Route(value = "/example1")
public class Example1 extends VerticalLayout {


    private  GraphService graphService;
    Grid<Customer> grid = new Grid<>(Customer.class);

    public Example1(GraphService graphService) {
        this.graphService = graphService;

        RouterLink back = new RouterLink("Home", MainView.class);

        setMargin(true);
        add(back);
        add(new Text("Example 1"));
        setSizeFull();
        configureGrid();
        add(getContent());
        updateList();

    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName");
//        grid.addColumn(customer -> customer.getFirstName());
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(1, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void updateList() {
        grid.setItems(this.graphService.getCustomer());
    }

}