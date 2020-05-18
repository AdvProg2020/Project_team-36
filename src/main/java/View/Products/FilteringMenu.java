package View.Products;

import Controllers.ObjectController;
import Controllers.ProductsController;
import Models.Filter;
import View.Menu;


import java.util.ArrayList;
import java.util.regex.Matcher;

public class FilteringMenu extends Menu {
    private ObjectController controller;
    private String filter;

    public FilteringMenu(String name, Menu parentMenu, ObjectController controller) {
        super(name, parentMenu);
        this.controller = controller;
        subMenus.put("show\\s+available\\s+filters", getAvailableFiltersMenu());
        subMenus.put("filter\\s+(.+)", getFilterMenu());
        subMenus.put("current\\s+filters", getCurrentFiltersMenu());
        subMenus.put("disable\\s+filter\\s+(.+)", getDisableFiltersMenu());
    }

    @Override
    public void help() {
        System.out.println("show available filters\n" +
                "filter [an available filter]\n" +
                "current filters\n" +
                "disable filter [a selected filter]");
    }

    private Menu getAvailableFiltersMenu() {
        return new Menu("available filters", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                int i = 1;
                for (String filter : controller.getAvailableFilters()) {
                    System.out.println(i + "." + filter);
                    i++;
                }
                System.out.println("Note:If you don't choose a category, you cannot have any special filter!");
            }
        };
    }

    private Menu getFilterMenu() {
        return new Menu("filterMenu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                if (filter.equalsIgnoreCase("category"))
                    getCategoryParameters();
                else {
                    try {
                        controller.setNewFilter(filter);
                    } catch (ProductsController.IntegerFieldException e) {
                        getRangeFilterParameters();
                    } catch (ProductsController.OptionalFieldException e) {
                        getOptionalFilterParameters();
                    } catch (ProductsController.NoFilterWithNameException e) {
                        System.err.println("There is no filter with this name available!");
                    }
                }
            }
        };
    }

    private void getCategoryParameters() {
        System.out.println("Enter name of category you want to filter by:");
        String input = scanner.nextLine().trim();
        try {
            controller.setCategoryFilter(input);
        } catch (ProductsController.NoCategoryWithName noCategoryWithName) {
            System.out.println("There is no category with this name!");
        }
    }

    private void getRangeFilterParameters() {
        String min;
        String max;
        System.out.println("enter the minimum of the range:");
        while (!(min = scanner.nextLine().trim()).matches("\\d+")) {
            if (min.matches("back"))
                return;
            else if (min.matches("login|logout|register"))
                sideCommands(min);
            else
                System.err.println("Invalid!");
        }
        while (!(max = scanner.nextLine().trim()).matches("\\d+")) {
            if (max.matches("back"))
                return;
            else if (max.matches("login|logout|register"))
                sideCommands(max);
            else
                System.err.println("Invalid!");
        }
        controller.setFilterRange(min, max);
    }

    private void getOptionalFilterParameters() {
        ArrayList<String> options = new ArrayList<>();
        String input;
        System.out.println("Write the options you need,then type \"end\"");
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("end")) {
            if (input.matches("register|login|logout"))
                sideCommands(input);
            else if (input.matches("back"))
                return;
            else
                options.add(input);
        }
        controller.setFilterOptions(options);
    }


    private Menu getCurrentFiltersMenu() {
        return new Menu("Current filters", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                System.out.println("The category you have chosen:");
                if (controller.getCurrentCategoryFilter() != null) {
                    System.out.println(controller.getCurrentCategoryFilter().getName());
                } else
                    System.out.println("You haven't chosen any category");
                ArrayList<Filter> filters = new ArrayList<>(controller.getCurrentFilters());
                if (filters.isEmpty())
                    System.out.println("you haven't chosen any filter yet!");
                else {
                    int i = 1;
                    for (Filter filter : filters) {
                        System.out.println(i + "." + filter);
                        i++;
                    }
                }
            }
        };
    }

    private Menu getDisableFiltersMenu() {
        return new Menu("disable filter menu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    controller.removeFilter(filter);
                } catch (ProductsController.NoFilterWithNameException e) {
                    System.err.println("There is no filter with this name!");
                }
            }
        };
    }

    @Override
    public void execute() {
        String input;
        Matcher matcher;
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("back")) {
            if (input.matches("help|register|login|logout"))
                sideCommands(input);
            else {
                Menu menu = null;
                for (String regex : this.subMenus.keySet()) {
                    if ((matcher = getMatcher(input, regex)).matches()) {
                        menu = subMenus.get(regex);
                        try {
                            this.filter = matcher.group(1);
                        } catch (Exception e) {
                            //do nothing
                        }
                        break;
                    }
                }
                if (menu == null)
                    System.err.println("Invalid command!");
                else
                    menu.execute();
            }
        }
        this.parentMenu.execute();
    }
}
