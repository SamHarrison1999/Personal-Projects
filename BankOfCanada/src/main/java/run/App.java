package run;

import operations.data.ReadDataFromAPI;
import utils.Query;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class App {

    // FXEURCAD FXHKDCAD FXINRCAD FXIDRCAD

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            ReadDataFromAPI.getObservationsBySeries(new String[] {"FXCADUSD"}, Optional.of(new Query(Optional.empty(), Optional.empty(), Optional.of("recent_weeks"), Optional.of(1), Optional.empty())));
            ReadDataFromAPI.getObservationsBySeries(new String[] {"FXAUDCAD"},  Optional.of(new Query(Optional.empty(), Optional.empty(), Optional.of("recent_weeks"), Optional.of(1), Optional.empty())));
        }
        else if (args.length > 0 && args.length < 7) {
            LocalDate startDate = LocalDate.parse(args[0]);
            LocalDate endDate = LocalDate.parse(args[1]);
            for (int index = 2; index < args.length; index ++) {
                ReadDataFromAPI.getObservationsBySeries(new String[] {args[index]},  Optional.of(new Query(Optional.of(startDate), Optional.of(endDate), Optional.empty(), Optional.empty(), Optional.empty())));
            }
        } else {
            System.err.println("Only up 4 series names are allowed");
            System.exit(1);
        }
    }
}