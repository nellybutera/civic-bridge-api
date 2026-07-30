package africa.civicbridge.api.config;

import africa.civicbridge.api.entity.*;
import africa.civicbridge.api.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository users;
    private final CivicContentRepository content;
    private final QuizRepository quizzes;
    private final ForumPostRepository forum;
    private final TrackerItemRepository tracker;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(AppUserRepository users, CivicContentRepository content, QuizRepository quizzes,
                       ForumPostRepository forum, TrackerItemRepository tracker, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.content = content;
        this.quizzes = quizzes;
        this.forum = forum;
        this.tracker = tracker;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Upsert (not count()==0 gated): also migrates any pre-existing plaintext
        // demo passwords from before hashing was added, without wiping real signups.
        upsertSeedUser("Amina Okafor", "admin@civicbridge.africa", "admin123", "Admin");
        upsertSeedUser("Kwame Mensah", "moderator@civicbridge.africa", "mod123", "Moderator");
        upsertSeedUser("Nia Uwimana", "youth@civicbridge.africa", "youth123", "Youth User");

        if (content.count() == 0) {
            content.save(new CivicContent(
                    "How a Bill Becomes Law in the East African Community",
                    "Parliamentary Process",
                    "A plain-language walkthrough of how the EAC Legislative Assembly drafts, debates, and passes regional legislation.",
                    "Every EAC law begins as a motion tabled by a member. It moves through committee review, two full readings before the assembly, and a final assent stage from partner-state heads of state.",
                    4));
            content.save(new CivicContent(
                    "Your Rights When Engaging With Local Government",
                    "Civic Rights",
                    "What every citizen aged 16+ is entitled to when petitioning, attending public hearings, or requesting public records.",
                    "Most national constitutions across the region guarantee the right to petition, the right to peaceful assembly, and increasingly a right of access to public information.",
                    5));
            content.save(new CivicContent(
                    "What the African Union Actually Does Day-to-Day",
                    "Regional Integration",
                    "Beyond the summits: the AU's standing organs, how they're funded, and how decisions trickle down to member states.",
                    "The AU Assembly meets twice a year, but the real day-to-day work happens in the Peace and Security Council, the Pan-African Parliament, and specialised technical committees.",
                    6));
            content.save(new CivicContent(
                    "Reading a National Budget Without an Economics Degree",
                    "Governance Literacy",
                    "A five-step method for finding out where your taxes actually go, using only a published budget PDF.",
                    "Start with the recurrent vs. development split, then find your sector of interest, then compare this year's allocation to last year's actual spend.",
                    5));
        }

        if (quizzes.count() == 0) {
            Quiz q1 = new Quiz("Governance Literacy Basics", 1L);
            q1.addQuestion(new Question(
                    "How many readings does a bill typically need before the EAC Legislative Assembly?",
                    List.of("One", "Two", "Three", "Four"), 1));
            q1.addQuestion(new Question(
                    "Who gives final assent to EAC regional legislation?",
                    List.of("The Secretary General alone", "Partner-state heads of state", "A single member of parliament", "The public via referendum"), 1));
            quizzes.save(q1);

            Quiz q2 = new Quiz("Know Your Civic Rights", 2L);
            q2.addQuestion(new Question(
                    "What right lets you formally raise an issue with a government office?",
                    List.of("Right to petition", "Right to silence", "Right to assembly", "Right to appeal"), 0));
            quizzes.save(q2);
        }

        if (forum.count() == 0) {
            forum.save(new ForumPost("Kwame Mensah", "Moderator", "Welcome — introduce your civic interest",
                    "Tell us one governance topic you want to understand better this month. Keep it respectful and specific."));
            forum.save(new ForumPost("Nia Uwimana", "Youth User", "Does anyone track county-level budget hearings?",
                    "I want to attend one in person but can't find a public schedule anywhere. Any tips?"));
        }

        if (tracker.count() == 0) {
            tracker.save(new TrackerItem("EAC Common Market Protocol — Free Movement of Persons", "In Force", 80,
                    "Adopted by most partner states; implementation gaps remain in labour permit reciprocity."));
            tracker.save(new TrackerItem("African Continental Free Trade Area (AfCFTA)", "Active Implementation", 55,
                    "Trading has begun under the Guided Trade Initiative; tariff schedules still being finalised."));
            tracker.save(new TrackerItem("AU Digital Transformation Strategy", "Early Stage", 30,
                    "Continental framework adopted; national digital ID interoperability still in pilot phase."));
            tracker.save(new TrackerItem("EAC Single Tourist Visa", "In Force", 90,
                    "Live across member states with strong adoption among regional travellers."));
        }
    }

    private void upsertSeedUser(String name, String email, String rawPassword, String role) {
        AppUser user = users.findByEmail(email).orElseGet(() -> new AppUser(name, email, null, role));
        boolean alreadyHashed = user.getPassword() != null && user.getPassword().startsWith("$2");
        if (!alreadyHashed) {
            user.setName(name);
            user.setRole(role);
            user.setPassword(passwordEncoder.encode(rawPassword));
            users.save(user);
        }
    }
}
