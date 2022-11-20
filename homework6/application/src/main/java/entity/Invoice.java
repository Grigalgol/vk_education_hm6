package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    private @NotNull int id;
    private @NotNull Date date;
    private @NotNull int organizationSender;

}
