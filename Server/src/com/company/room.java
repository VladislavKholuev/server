package com.company;

import java.util.Vector;

public class room {
    private String room_name;
    private Vector<person> room_isers = new Vector<>();

    room(String new_room){
        this.room_name = new_room;
    }

    public void SetUser(person new_user_in_room){
        room_isers.add(new_user_in_room);
    }
    public Vector get_Users_spisok_in_room(){
        return room_isers;
    }
    public String getRoom_name()
    {
     return room_name;
    }
    public void left_from_room(person left_person){
        Vector<person> update_vec = new Vector<>();
        for(person per: room_isers)
        {
            if(per.getLogin() != left_person.getLogin())
            {
                update_vec.add(per);
            }
        }
        room_isers = update_vec;
//        for(person per: room_isers)
//        {
//            System.out.println(per.getLogin() + "  " + left_person.getLogin());
//            if(per.getLogin().equals(left_person.getLogin()))
//            {
//             room_isers.removeElement(per);
//            }

//        }
    }
}
