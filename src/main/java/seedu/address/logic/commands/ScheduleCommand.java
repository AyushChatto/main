package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;

import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

public class ScheduleCommand extends Command {

    public static final String COMMAND_WORD = "schedule";
    public static final String COMMAND_ALIAS = "sch";

    public static final String MESSAGE_SCHEDULING_SUCCESS = "Meeting added";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Schedules a value with the person at the given"
            + "index number. "
            + "Existing values will be overwritten by new values. \n"
            + "Parameters: INDEX (must be a positive integer)"
            + "[" + PREFIX_MEETING + "MEETING TIME]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_MEETING + "3112181630";

    private final Index index;
    private final Meeting meeting;

    public ScheduleCommand(Index index, Meeting meeting) {
        requireNonNull(index);
        requireNonNull(meeting);
        this.index = index;
        this.meeting = meeting;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToSchedule = lastShownList.get(index.getZeroBased());
        Person scheduledPerson = createScheduledPerson(personToSchedule, meeting);

        model.updatePerson(personToSchedule, scheduledPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_SCHEDULING_SUCCESS));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToSchedule}
     * updated with the value {@code value}.
     */
    private static Person createScheduledPerson(Person personToSchedule, Meeting meeting) {
        assert personToSchedule != null;

        Name name = personToSchedule.getName();
        Phone phone = personToSchedule.getPhone();
        Email email = personToSchedule.getEmail();
        Address address = personToSchedule.getAddress();
        Set<Tag> tags = personToSchedule.getTags();

        return new Person(name, phone, email, address, tags, meeting);
    }


}
