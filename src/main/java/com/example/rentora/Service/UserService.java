package com.example.rentora.Service;

import com.example.rentora.Model.User;
import com.example.rentora.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public int addUser(User user) {
        User existingUser = userRepository.findUserByEmail(user.getEmail());
        if (existingUser != null) {
            return 1;
        }


        userRepository.save(user);


        String subject = "مرحباً بك في منصة Rentora!";
        String body = "أهلاً " + user.getName() + "،\n\n" +
                "تم إنشاء حسابك بنجاح في منصة Rentora لتأجير المنتجات.\n" +
                "نتمنى لك تجربة ممتعة!";

        emailService.sendEmail(user.getEmail(), subject, body);

        return 0;
    }

    public boolean updateUser(Integer id, User user){
        User oldUser = userRepository.findUserById(id);
        if (oldUser == null) {
            return false;
        }

        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        oldUser.setPhone(user.getPhone());

        userRepository.save(oldUser);
        return true;
    }

    public boolean deleteUser(Integer id){
        User oldUser = userRepository.findUserById(id);

        if(oldUser==null){
            return false;
        }
        userRepository.delete(oldUser);
        return true ;
    }
    public int requestNeededItem(Integer renterId, String itemDescription) {
        User renter = userRepository.findUserById(renterId);
        if (renter == null) {
            return 1;
        }


        List<User> allUsers = userRepository.findAll();

        String subject = "Rentora - طلب منتج جديد من أحد المستخدمين!";
        String body = "أهلاً بك،\n\n" +
                "المستخدم (" + renter.getName() + ") يبحث عن منتج بالمواصفات التالية:\n\n" +
                "📝 Description: " + itemDescription + "\n\n" +
                "إذا كان لديك هذا المنتج وتود تأجيره، يمكنك التواصل معه عبر البريد: " + renter.getEmail();

        for (User user : allUsers) {
            if (user.getEmail() != null && !user.getId().equals(renterId)) {
                emailService.sendEmail(user.getEmail(), subject, body);
            }
        }

        return 0;
    }
}
