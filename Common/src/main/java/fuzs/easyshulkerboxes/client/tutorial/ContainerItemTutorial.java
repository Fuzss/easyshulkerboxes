package fuzs.easyshulkerboxes.client.tutorial;

import net.minecraft.client.Options;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * simply copied from {@link net.minecraft.client.tutorial.BundleTutorial} with minor adjustments for any container item
 */
public class ContainerItemTutorial {
   private final Tutorial tutorial;
   private final Options options;
   private final Predicate<ItemStack> filter;
   private final Component itemComponent;
   @Nullable
   private TutorialToast toast;

   public ContainerItemTutorial(Tutorial p_175003_, Options p_175004_, Predicate<ItemStack> filter, Component itemComponent) {
      this.tutorial = p_175003_;
      this.options = p_175004_;
      this.filter = filter;
      this.itemComponent = itemComponent;
   }

   private void showToast() {
      if (this.toast != null) {
         this.tutorial.removeTimedToast(this.toast);
      }

      Component component = Component.translatable("tutorial.container.itemInsert.title", this.itemComponent);
      Component component1 = Component.translatable("tutorial.bundleInsert.description");
      this.toast = new TutorialToast(TutorialToast.Icons.RIGHT_CLICK, component, component1, true);
      this.tutorial.addTimedToast(this.toast, 160);
   }

   private void clearToast() {
      if (this.toast != null) {
         this.tutorial.removeTimedToast(this.toast);
         this.toast = null;
      }

      if (!this.options.hideBundleTutorial) {
         this.options.hideBundleTutorial = true;
         this.options.save();
      }

   }

   public void onInventoryAction(ItemStack p_175007_, ItemStack p_175008_, ClickAction p_175009_) {
      if (!this.options.hideBundleTutorial) {
         if (!p_175007_.isEmpty() && this.filter.test(p_175008_)) {
            if (p_175009_ == ClickAction.PRIMARY) {
               this.showToast();
            } else if (p_175009_ == ClickAction.SECONDARY) {
               this.clearToast();
            }
         } else if (this.filter.test(p_175007_) && !p_175008_.isEmpty() && p_175009_ == ClickAction.SECONDARY) {
            this.clearToast();
         }

      }
   }
}