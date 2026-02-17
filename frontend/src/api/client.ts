import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

const productsClient = axios.create({
    baseURL: 'http://localhost:8081/api/v1',
});

const inventoryClient = axios.create({
    baseURL: 'http://localhost:8082/api/v1',
});

// Request Interceptor to add Auth Token
const addAuthInterceptor = (client: any) => {
    client.interceptors.request.use((config: any) => {
        const authStore = useAuthStore();
        if (authStore.token) {
            config.headers.Authorization = `Bearer ${authStore.token}`;
        }
        return config;
    }, (error: any) => {
        return Promise.reject(error);
    });
};

addAuthInterceptor(productsClient);
addAuthInterceptor(inventoryClient);

export { productsClient, inventoryClient };
