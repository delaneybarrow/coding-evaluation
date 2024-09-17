package com.aa.act.interview.org;

import java.lang.StackWalker.Option;
import java.util.Optional;

public abstract class Organization {

    private Position root;
    
    public Organization() {
        root = createOrganization();
    }
    
    protected abstract Position createOrganization();
    
    /**
     * hire the given person as an employee in the position that has that title
     * 
     * @param person
     * @param title
     * @return the newly filled position or empty if no position has that title
     */
    public Optional<Position> hire(Name person, String title) {
        // error handling, validate function parameters
        if (person == null) {
            throw new IllegalArgumentException("person cannot be null");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title cannot be null");
        }

        // find position to fill using helper method using root as top of heirarchy
        Position positionToFill = findPosition(root, title); 

        // if position is not filled, assign employee to that position
        if (positionToFill != null) {
            // create a new employee with a unique identifier and assign them to correct position
            Employee newEmployee = new Employee(person.hashCode(), person);
            positionToFill.setEmployee(Optional.of(newEmployee));

            // return position with employee 
            return Optional.of(positionToFill);
        }
        // if there is no available position, return empty
        return Optional.empty();
    }

    // helper function to recursively find position with a matching title
    private Position findPosition(Position currentPosition, String title) {
        // base case
        if (currentPosition.getTitle() == title) {
            return currentPosition;
        }

        // iterate through organization heirarchy and recursively search for matching position
        for (Position directReport: currentPosition.getDirectReports()) {
            Position foundPosition = findPosition(directReport, title);
            if (foundPosition != null) {
                return foundPosition;
            }
        }

        // if no matching position was found, return null
        return null;
    }

    @Override
    public String toString() {
        return printOrganization(root, "");
    }
    
    private String printOrganization(Position pos, String prefix) {
        StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
        for(Position p : pos.getDirectReports()) {
            sb.append(printOrganization(p, prefix + "  "));
        }
        return sb.toString();
    }
}
