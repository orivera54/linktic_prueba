import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

export const useAuthStore = defineStore('auth', () => {
    const token = ref<string | null>(localStorage.getItem('token'));
    const user = ref<string | null>(localStorage.getItem('user'));
    const router = useRouter();

    const isAuthenticated = computed(() => !!token.value);

    const login = async (email: string, password: string) => {
        // Simulate API call
        return new Promise<boolean>((resolve) => {
            setTimeout(() => {
                if (email && password) { // Simple validation
                    const mockToken = 'mock-jwt-token-' + Date.now();
                    token.value = mockToken;
                    user.value = email;

                    localStorage.setItem('token', mockToken);
                    localStorage.setItem('user', email);
                    resolve(true);
                } else {
                    resolve(false);
                }
            }, 500);
        });
    };

    const logout = () => {
        token.value = null;
        user.value = null;
        localStorage.removeItem('token');
        localStorage.removeItem('user');

        // Force reload to clear any sensitive state in other stores if needed
        // window.location.reload(); 
        // Or just redirect
        if (router) router.push('/login');
    };

    return {
        token,
        user,
        isAuthenticated,
        login,
        logout
    };
});
