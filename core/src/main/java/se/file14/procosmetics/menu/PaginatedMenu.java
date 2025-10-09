package se.file14.procosmetics.menu;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.menu.ClickableItem;
import se.file14.procosmetics.api.user.User;

import java.util.*;
import java.util.function.Function;

public abstract class PaginatedMenu extends MenuImpl {

    private final int top;
    private final int bottom;
    private final int left;
    private final int right;
    private final List<PaginatedItem> items = new ArrayList<>();
    private final IntList slots = new IntArrayList();
    protected int page = 1;
    private Function<PageInfo, ItemStack> nextPageItem;
    private Function<PageInfo, ItemStack> previousPageItem;
    private int nextPageSlot = -1;
    private int previousPageSlot = -1;
    private Comparator<PaginatedItem> sorting;
    private boolean sorted;

    public PaginatedMenu(ProCosmetics plugin, User user, Component title, int rows,
                         int topPadding, int bottomPadding, int leftPadding, int rightPadding) {
        super(plugin, user, title, rows);
        this.top = topPadding;
        this.bottom = bottomPadding;
        this.left = leftPadding;
        this.right = rightPadding;

        // Initialize slots for pagination
        for (int i = 0; i < getContainerSize(); i++) {
            int row = i / (9 - left - right);
            int column = i % (9 - left - right);
            slots.add((top + row) * 9 + left + column);
        }
    }

    public PaginatedMenu(ProCosmetics plugin, User user, Component title, int rows, int padding) {
        this(plugin, user, title, rows, padding, padding, padding, padding);
    }

    public PaginatedMenu(ProCosmetics plugin, User user, Component title, int rows) {
        this(plugin, user, title, rows, 1);
    }

    public final int getContainerSize() {
        return (9 - left - right) * (getRows() - top - bottom);
    }

    @Override
    protected void addItems() {
        drawPaginatedItems();
        addNavigationItems();
        addCustomItems();
    }

    protected void drawPaginatedItems() {
        if (sorting != null && !sorted) {
            items.sort(sorting);
            sorted = true;
        }

        List<List<PaginatedItem>> pages = Lists.partition(items, getContainerSize());
        page = Math.max(1, Math.min(pages.size(), page));
        List<PaginatedItem> pageContent = pages.isEmpty() ? Collections.emptyList() : pages.get(page - 1);
        PageInfo pageInfo = new PageInfo(page, pages.size());

        // Handle next page button
        if (nextPageSlot != -1) {
            if (page < pages.size() && nextPageItem != null) {
                setItem(nextPageSlot, nextPageItem.apply(pageInfo), event -> {
                    page = event.isShiftClick() ? pages.size() : page + 1;
                    playClickSound();
                    refresh();
                });
            } else {
                removeItem(nextPageSlot);
            }
        }

        // Handle previous page button
        if (previousPageSlot != -1) {
            if (page > 1 && previousPageItem != null) {
                setItem(previousPageSlot, previousPageItem.apply(pageInfo), event -> {
                    page = event.isShiftClick() ? 1 : page - 1;
                    playClickSound();
                    refresh();
                });
            } else {
                removeItem(previousPageSlot);
            }
        }

        // Clear existing paginated slots
        for (int i = 0; i < slots.size(); i++) {
            removeItem(slots.getInt(i));
        }

        // Add paginated items
        for (int i = 0; i < pageContent.size(); i++) {
            PaginatedItem item = pageContent.get(i);
            if (item != null) {
                int slot = slots.getInt(i);
                setItem(slot, item.getItemStack(), item.getClickHandler());
            }
        }
    }

    protected void addNavigationItems() {
        // Override in subclasses for custom navigation items
    }

    protected void addCustomItems() {
        // Override in subclasses for custom items
    }

    public void refresh() {
        clear();
        addItems();

        fillEmptySlots(inventory, getFillEmptySlotsItem());
    }

    public void openPage(int page) {
        this.page = page;
        if (isValid()) {
            refresh();
        } else {
            // Call the parent open method directly to avoid circular calls
            super.open();
        }
    }

    public void addPaginatedItem(PaginatedItem item) {
        items.add(item);
        sorted = false;
    }

    public void addPaginatedItems(Collection<PaginatedItem> items) {
        this.items.addAll(items);
        sorted = false;
    }

    public void removePaginatedItem(PaginatedItem item) {
        items.remove(item);
    }

    public void clearPaginatedItems() {
        items.clear();
    }

    public int getCurrentPage() {
        return page;
    }

    public int getPageCount() {
        if (items.isEmpty()) return 1;
        return (int) Math.ceil((double) items.size() / getContainerSize());
    }

    public Function<PageInfo, ItemStack> getNextPageItemStack() {
        return nextPageItem;
    }

    public void setNextPageItemStack(Function<PageInfo, ItemStack> item) {
        nextPageItem = item;
    }

    public Function<PageInfo, ItemStack> getPreviousPageItemStack() {
        return previousPageItem;
    }

    public void setPreviousPageItemStack(Function<PageInfo, ItemStack> item) {
        previousPageItem = item;
    }

    public int getNextPageSlot() {
        return nextPageSlot;
    }

    public void setNextPageSlot(int nextPageSlot) {
        this.nextPageSlot = nextPageSlot;
    }

    public int getPreviousPageSlot() {
        return previousPageSlot;
    }

    public void setPreviousPageSlot(int previousPageSlot) {
        this.previousPageSlot = previousPageSlot;
    }

    public Comparator<PaginatedItem> getSorting() {
        return sorting;
    }

    public void setSorting(Comparator<PaginatedItem> sorting) {
        this.sorting = sorting;
        sorted = false;
    }

    public static class PaginatedItem {
        private final ItemStack itemStack;
        private final ClickableItem clickHandler;

        public PaginatedItem(ItemStack itemStack, ClickableItem clickHandler) {
            this.itemStack = itemStack;
            this.clickHandler = clickHandler;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public ClickableItem getClickHandler() {
            return clickHandler;
        }
    }

    public static class PageInfo {
        private final int currentPage;
        private final int pageCount;

        public PageInfo(int currentPage, int pageCount) {
            this.currentPage = currentPage;
            this.pageCount = pageCount;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getPageCount() {
            return pageCount;
        }
    }
}