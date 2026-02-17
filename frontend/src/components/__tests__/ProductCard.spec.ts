import { describe, it, expect } from 'vitest';
import { mount } from '@vue/test-utils';
import ProductCard from '../ProductCard.vue';
import type { Product } from '@/types';

describe('ProductCard.vue', () => {
    const product: Product = {
        id: '123',
        name: 'Test Product',
        price: 99.99,
        sku: 'TEST-SKU',
        status: 'ACTIVE',
        createdAt: '2023-01-01T00:00:00Z',
        updatedAt: '2023-01-01T00:00:00Z'
    };

    it('renders product details correctly', () => {
        const wrapper = mount(ProductCard, {
            props: { product }
        });

        expect(wrapper.text()).toContain('Test Product');
        expect(wrapper.text()).toContain('TEST-SKU');
        expect(wrapper.text()).toContain('$99.99');
        expect(wrapper.text()).toContain('Activo');
    });

    it('emits addToCart event when button is clicked', async () => {
        const wrapper = mount(ProductCard, {
            props: { product }
        });

        await wrapper.find('button').trigger('click');

        expect(wrapper.emitted()).toHaveProperty('addToCart');
        expect(wrapper.emitted('addToCart')![0][0]).toEqual(product);
    });
});
