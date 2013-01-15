package agpro.data.contact;

import agpro.data.auth.HasUser;
import agpro.data.auth.ResultUser;

public class Contact implements HasUser<Contact> {
    public String name;
    public String phone;
    public String email;
    public String subject;
    public String message;

    public Contact(String message, String subject, String email, String phone, String name) {
        this.message = message;
        this.subject = subject;
        this.email = email;
        this.phone = phone;
        this.name = name;
    }

    public ResultUser<Integer> get() {
        return ResultUser.error();
    }
}
