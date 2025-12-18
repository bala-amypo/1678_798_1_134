@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest r) {
        service.register(r);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest r) {
        return service.login(r);
    }
}
