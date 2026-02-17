import { describe, it, expect, beforeEach, vi } from 'vitest';
import { SimpleCache } from '@/utils/cache';

describe('SimpleCache', () => {
    let cache: SimpleCache;

    beforeEach(() => {
        cache = new SimpleCache();
        vi.useFakeTimers();
    });

    it('should store and retrieve data', () => {
        cache.set('key', 'value', 1000);
        expect(cache.get('key')).toBe('value');
    });

    it('should return null if key does not exist', () => {
        expect(cache.get('ghost')).toBeNull();
    });

    it('should expire data after TTL', () => {
        cache.set('key', 'value', 1000);

        // Fast-forward time
        vi.advanceTimersByTime(1500);

        expect(cache.get('key')).toBeNull();
    });

    it('should delete data', () => {
        cache.set('key', 'value', 1000);
        cache.delete('key');
        expect(cache.get('key')).toBeNull();
    });

    it('should clear all data', () => {
        cache.set('k1', 'v1', 1000);
        cache.set('k2', 'v2', 1000);
        cache.clear();
        expect(cache.get('k1')).toBeNull();
        expect(cache.get('k2')).toBeNull();
    });
});
